package org.example.ecommerce.product.api;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.ApiResponse;
import org.example.ecommerce.common.CommonApi;
import org.example.ecommerce.exception.StatusCode;
import org.example.ecommerce.product.dto.request.CreateProductRequest;
import org.example.ecommerce.product.service.ProductService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateProductApi implements CommonApi<ApiResponse,  CreateProductRequest> {
    private final ProductService productService;
    private static final String CREATE_PRODUCT_SUCCESS_MESSAGE = "Product created successfully";
    @Override
    public ApiResponse execute(CreateProductRequest request) {
        productService.addProduct(request);
        return new ApiResponse(StatusCode.SUCCESS.getCode(), CREATE_PRODUCT_SUCCESS_MESSAGE);
    }
}
