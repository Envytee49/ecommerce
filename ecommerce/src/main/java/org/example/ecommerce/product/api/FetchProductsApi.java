package org.example.ecommerce.product.api;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.CommonApi;
import org.example.ecommerce.common.ResponseData;
import org.example.ecommerce.exception.StatusCode;
import org.example.ecommerce.product.dto.request.FetchProductsRequest;
import org.example.ecommerce.product.dto.response.ProductsResponse;
import org.example.ecommerce.product.service.ProductService;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FetchProductsApi implements CommonApi<ResponseData<ProductsResponse>, FetchProductsRequest> {
    private final ProductService productService;
    @Override
    public ResponseData<ProductsResponse> execute(FetchProductsRequest fetchProductsRequest) {
        ProductsResponse productsResponse = productService.getAllProducts(fetchProductsRequest);
        return new ResponseData<>(StatusCode.SUCCESS.getCode(), null, productsResponse);
    }
}
