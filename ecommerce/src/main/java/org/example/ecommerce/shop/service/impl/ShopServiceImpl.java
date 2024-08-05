package org.example.ecommerce.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.constants.TBEnum;
import org.example.ecommerce.common.constants.TSPEnum;
import org.example.ecommerce.common.dto.PageDtoIn;
import org.example.ecommerce.common.dto.PageDtoOut;
import org.example.ecommerce.common.util.Utils;
import org.example.ecommerce.configuration.security.SecurityUtils;
import org.example.ecommerce.order.model.Order;
import org.example.ecommerce.order.repository.OrderRepository;
import org.example.ecommerce.shop.dto.request.RevenueRequest;
import org.example.ecommerce.shop.dto.response.RevenueResponse;
import org.example.ecommerce.shop.dto.response.TopBuyer;
import org.example.ecommerce.shop.dto.response.TopSellingProduct;
import org.example.ecommerce.shop.projection.TBProjection;
import org.example.ecommerce.shop.projection.TSPProjection;
import org.example.ecommerce.shop.repository.ShopRepository;
import org.example.ecommerce.shop.service.ShopService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {
    private final ShopRepository shopRepository;
    private final OrderRepository orderRepository;
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
                                .totalAmount(order.getTotal())
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
        Sort sort = Utils.getSortStrategy(sortBy, TSPEnum.class, sortDirection);
        Pageable pageRequest = PageRequest.of(pageDtoIn.getPage() - 1, pageDtoIn.getSize(), sort);
        String uuidShop = shopRepository.findByUuidSeller(SecurityUtils.getCurrentUserUuid()).getUuidShop();
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
        Sort sort = Utils.getSortStrategy(sortBy, TBEnum.class, sortDirection);
        Pageable pageRequest = PageRequest.of(pageDtoIn.getPage() - 1, pageDtoIn.getSize(), sort);
        String uuidShop = shopRepository.findByUuidSeller(SecurityUtils.getCurrentUserUuid()).getUuidShop();
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

}
