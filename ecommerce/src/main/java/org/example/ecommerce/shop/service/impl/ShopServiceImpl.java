package org.example.ecommerce.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.constants.sortby.TBEnum;
import org.example.ecommerce.common.constants.sortby.TSPEnum;
import org.example.ecommerce.common.dto.PageDtoIn;
import org.example.ecommerce.common.dto.PageDtoOut;
import org.example.ecommerce.common.util.Utils;
import org.example.ecommerce.configuration.security.SecurityUtils;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.order.model.Order;
import org.example.ecommerce.order.repository.OrderRepository;
import org.example.ecommerce.shop.dto.request.CreateShopRequest;
import org.example.ecommerce.shop.dto.request.RevenueRequest;
import org.example.ecommerce.shop.dto.response.RevenueResponse;
import org.example.ecommerce.shop.dto.response.TopBuyer;
import org.example.ecommerce.shop.dto.response.TopSellingProduct;
import org.example.ecommerce.shop.model.Shop;
import org.example.ecommerce.shop.model.ShopAddress;
import org.example.ecommerce.shop.projection.TBProjection;
import org.example.ecommerce.shop.projection.TSPProjection;
import org.example.ecommerce.shop.repository.ShopAddressRepository;
import org.example.ecommerce.shop.repository.ShopRepository;
import org.example.ecommerce.shop.service.ShopService;
import org.example.ecommerce.user.service.UserManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {
    private static final Logger log = LoggerFactory.getLogger(ShopServiceImpl.class);
    private final ShopRepository shopRepository;
    private final OrderRepository orderRepository;
    private final ShopAddressRepository shopAddressRepository;
    private final UserManagementService userManagementService;

    @Override
    public PageDtoOut<RevenueResponse> getRevenue(RevenueRequest request) {
        PageDtoIn pageDtoIn = request.getPageDtoIn();
        String uuidShop = shopRepository.findByUuidSeller(SecurityUtils.getCurrentUserUuid()).getUuidShop();
        Pageable pageRequest = PageRequest.of(pageDtoIn.getPage() - 1, pageDtoIn.getSize());
        Page<Order> orders = orderRepository
                .findByUuidShopAndCreatedDateBetween(
                        uuidShop,
                        request.getFromDate().atStartOfDay(),
                        request.getToDate().atStartOfDay(),
                        pageRequest);
        List<RevenueResponse> revenueResponse = orders
                .map(order ->
                        RevenueResponse
                                .builder()
                                .orderDate(order.getCreatedDate().toLocalDate())
                                .uuidOrder(order.getUuidOrder())
                                .totalAmount(order.getTotalPayment())
                                .build()
                )
                .toList();
        return PageDtoOut.from(
                pageDtoIn.getPage(),
                pageDtoIn.getSize(),
                orders.getTotalElements(),
                revenueResponse
        );
    }

    @Override
    public PageDtoOut<TopSellingProduct> getTopSellingProducts(PageDtoIn pageDtoIn, List<String> sortBy, String sortDirection) {
        log.info("Get top selling product");
        Sort sort = Utils.getSortStrategy(sortBy, TSPEnum.class, sortDirection);
        Pageable pageRequest = PageRequest.of(pageDtoIn.getPage() - 1, pageDtoIn.getSize(), sort);
        log.info("find uuid shop");
        String uuidShop = shopRepository.findByUuidSeller(SecurityUtils.getCurrentUserUuid()).getUuidShop();
        log.info("find top selling products");
        Page<TSPProjection> tspProjections =  shopRepository.findTopSellingProducts(uuidShop, pageRequest);

        List<TopSellingProduct> topSellingProducts = tspProjections.getContent().stream().map(
                tspProjection -> TopSellingProduct.builder()
                        .productTitle(tspProjection.getProductTitle())
                        .uuidProduct(tspProjection.getUuidProduct())
                        .revenue(tspProjection.getRevenue())
                        .orderCounts(tspProjection.getOrderCounts())
                        .unitsSold(tspProjection.getUnitsSold())
                        .build()
        ).toList();
        return PageDtoOut.from(
                pageDtoIn.getPage(),
                pageDtoIn.getSize(),
                tspProjections.getTotalElements(),
                topSellingProducts
        );
    }

    @Override
    public PageDtoOut<TopBuyer> getTopBuyers(PageDtoIn pageDtoIn, List<String> sortBy, String sortDirection) {
        log.info("Get top buyers");
        Sort sort = Utils.getSortStrategy(sortBy, TBEnum.class, sortDirection);
        Pageable pageRequest = PageRequest.of(pageDtoIn.getPage() - 1, pageDtoIn.getSize(), sort);
        log.info("find uuidShop");
        String uuidShop = shopRepository.findByUuidSeller(SecurityUtils.getCurrentUserUuid()).getUuidShop();
        log.info("find top buyers");
        Page<TBProjection> tbProjections = shopRepository.findTopBuyers(uuidShop,pageRequest);
        List<TopBuyer> topBuyers = tbProjections
                .getContent()
                .stream()
                .map(tbProjection -> TopBuyer
                        .builder()
                        .uuidUser(tbProjection.getUuidUser())
                        .username(tbProjection.getUsername())
                        .orderCounts(tbProjection.getOrderCounts())
                        .totalSpend(tbProjection.getTotalSpend())
                        .build())
                .toList();
        return PageDtoOut.from(
                pageDtoIn.getPage(),
                pageDtoIn.getSize(),
                tbProjections.getTotalElements(),
                topBuyers
        );
    }

    @Override
    @Transactional
    public void createShop(CreateShopRequest request) {
        String name = request.getName();
        if(shopRepository.findByName(name).isPresent()) throw new AppException(ErrorCode.SHOP_NAME_EXISTED);
        Shop shop = Shop.builder().name(name).uuidSeller(SecurityUtils.getCurrentUserUuid()).build();
        shopRepository.save(shop);
        ShopAddress shopAddress = ShopAddress.builder()
                .city(request.getCity())
                .district(request.getDistrict())
                .street(request.getStreet())
                .mobile(request.getMobile())
                .postalCode(request.getPostalCode())
                .sellerName(request.getSellerName())
                .uuidShop(shop.getUuidShop())
                .build();
        shopAddressRepository.save(shopAddress);
        userManagementService.updateSellerRole();
    }

}
