package org.example.ecommerce.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNKNOWN(1, "Unknown Error"),

    INVALID_PARAMETER_DATA(998, null),

    PARAMETER_NOT_VALID(999, null),

    INVALID_PARAMETER_TYPE(1000, "Parameter type is not suitable."),

    INVALID_DATA_FORMAT(1001, "Invalid data format."),

    NO_RESOURCE_FOUND(1002, "No resource found."),

    PRODUCT_NOT_FOUND(1003, "Product Not Found"),

    BRAND_NOT_FOUND(1004, "Brand Not Found");


    private final int code;

    private final String message;

}
