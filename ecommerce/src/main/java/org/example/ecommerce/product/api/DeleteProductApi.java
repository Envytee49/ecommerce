package org.example.ecommerce.product.api;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.ApiResponse;
import org.example.ecommerce.common.CommonApi;
import org.example.ecommerce.exception.StatusCode;
import org.example.ecommerce.product.service.ProductService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteProductApi implements CommonApi<ApiResponse, String> {
    private final ProductService productService;
    private static final String DELETE_PRODUCT_SUCCESS_MESSAGE = "Product deleted successfully";
    @Override
    public ApiResponse execute(String uuidProduct) {
        productService.deleteProduct(uuidProduct);
        return new ApiResponse(StatusCode.SUCCESS.getCode(), DELETE_PRODUCT_SUCCESS_MESSAGE);
    }
}
