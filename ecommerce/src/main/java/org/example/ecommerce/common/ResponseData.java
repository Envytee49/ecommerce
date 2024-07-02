package org.example.ecommerce.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseData<T> extends ApiResponse{
    private T data;

    public ResponseData(int code, String message, T data) {
        super(code, message);
        this.data = data;
    }
}
