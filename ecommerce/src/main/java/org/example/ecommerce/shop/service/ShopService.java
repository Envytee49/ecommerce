package org.example.ecommerce.shop.service;

import org.example.ecommerce.common.dto.PageDtoIn;
import org.example.ecommerce.common.dto.PageDtoOut;
import org.example.ecommerce.shop.dto.request.CreateShopRequest;
import org.example.ecommerce.shop.dto.request.RevenueRequest;
import org.example.ecommerce.shop.dto.response.RevenueResponse;
import org.example.ecommerce.shop.dto.response.TopBuyer;
import org.example.ecommerce.shop.dto.response.TopSellingProduct;

import java.util.List;

public interface ShopService {
    PageDtoOut<RevenueResponse> getRevenue(RevenueRequest request);
    PageDtoOut<TopSellingProduct> getTopSellingProducts(PageDtoIn pageDtoIn, List<String> sortBy, String sortDirection);
    PageDtoOut<TopBuyer> getTopBuyers(PageDtoIn pageDtoIn, List<String> sortBy, String sortDirection);
    void createShop(CreateShopRequest request);
}
