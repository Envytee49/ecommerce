package org.example.ecommerce.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_PARAMETER_DATA(998, null),

    PARAMETER_NOT_VALID(999, null),

    INVALID_PARAMETER_TYPE(1000, "Parameter type is not suitable."),

    NO_RESOURCE_FOUND(1001, "Not Found"),

    INVALID_DATE_FORMAT(1002, "Invalid date format. Please use yyyy-MM-dd HH:mm:ss");

    private final int code;

    private final String message;

}
