package org.example.ecommerce.shop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.ApiResponse;
import org.example.ecommerce.common.controller.AbstractController;
import org.example.ecommerce.common.dto.PageDtoIn;
import org.example.ecommerce.shop.dto.request.CreateShopRequest;
import org.example.ecommerce.shop.dto.request.RevenueRequest;
import org.example.ecommerce.shop.service.ShopService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shops")
@RequiredArgsConstructor
public class ShopController extends AbstractController {
    private final ShopService shopService;

    @GetMapping("/revenue")
    public ApiResponse<?> getRevenue(@RequestBody @Valid RevenueRequest request) {
        return respond(() -> shopService.getRevenue(request));
    }

    @GetMapping("/top-selling-product")
    public ApiResponse<?> getTopSellingProducts(@RequestBody @Valid PageDtoIn pageDtoIn,
                                                @RequestParam List<String> sortBy,
                                                @RequestParam String sortDirection) {
        return respond(() -> shopService.getTopSellingProducts(pageDtoIn, sortBy, sortDirection));
    }

    @GetMapping("/top-buyer")
    public ApiResponse<?> getTopBuyers(@RequestBody @Valid PageDtoIn pageDtoIn,
                                       @RequestParam List<String> sortBy,
                                       @RequestParam String sortDirection) {
        return respond(() -> shopService.getTopBuyers(pageDtoIn, sortBy, sortDirection));
    }

    @PostMapping
    public ApiResponse<?> createShop(CreateShopRequest request) {
        return respond(() -> shopService.createShop(request), "Shop created");
    }

}
