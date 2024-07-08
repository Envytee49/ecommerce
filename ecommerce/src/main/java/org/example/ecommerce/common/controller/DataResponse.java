package org.example.ecommerce.common.controller;
@FunctionalInterface
public interface DataResponse<T> {
    T execute();
}
