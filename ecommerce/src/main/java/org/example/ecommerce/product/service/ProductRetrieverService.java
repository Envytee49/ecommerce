package org.example.ecommerce.product.service;

import org.example.ecommerce.common.dto.PageDtoIn;
import org.example.ecommerce.common.dto.PageDtoOut;
import org.example.ecommerce.product.dto.response.ProductDetailResponse;
import org.example.ecommerce.product.dto.response.ProductResponse;

public interface ProductRetrieverService {
    PageDtoOut<ProductResponse> getAll(PageDtoIn pageDtoIn);
    ProductDetailResponse getByUuid(String uuidProduct);
}
