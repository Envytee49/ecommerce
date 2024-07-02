package org.example.ecommerce.product.api;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.CommonApi;
import org.example.ecommerce.common.ResponseData;
import org.example.ecommerce.exception.StatusCode;
import org.example.ecommerce.product.dto.response.ProductDetailResponse;
import org.example.ecommerce.product.service.ProductService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FetchProductDetailApi implements CommonApi<ResponseData<ProductDetailResponse>, String> {
    private final ProductService productService;

    @Override
    public ResponseData<ProductDetailResponse> execute(String uuidProduct) {
        ProductDetailResponse productDetailResponse =
                productService.getProductByUuid(uuidProduct);
        return new ResponseData<>(StatusCode.SUCCESS.getCode(),null, productDetailResponse);
    }
}
