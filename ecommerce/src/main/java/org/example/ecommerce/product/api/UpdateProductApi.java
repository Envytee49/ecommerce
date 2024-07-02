package org.example.ecommerce.product.api;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.ApiResponse;
import org.example.ecommerce.common.CommonApi;
import org.example.ecommerce.exception.StatusCode;
import org.example.ecommerce.product.dto.request.UpdateProductRequest;
import org.example.ecommerce.product.service.ProductService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateProductApi implements CommonApi<ApiResponse, UpdateProductRequest> {
    private final ProductService productService;
    private static final String UPDATE_PRODUCT_SUCCESS_MESSAGE = "Product updated successfully";

    @Override
    public ApiResponse execute(UpdateProductRequest request) {
        productService.updateProduct(request);
        return new ApiResponse(StatusCode.SUCCESS.getCode(), UPDATE_PRODUCT_SUCCESS_MESSAGE);
    }
}
