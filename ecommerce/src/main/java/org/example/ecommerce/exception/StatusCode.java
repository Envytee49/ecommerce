package org.example.ecommerce.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusCode {

    SUCCESS(1),

    UNKNOWN(2);

    private final int code;

}
