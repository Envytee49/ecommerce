package org.example.ecommerce.common;

public interface CommonApi<T extends ApiResponse, U> {
    T execute(U request);
}
