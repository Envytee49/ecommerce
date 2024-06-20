package org.example.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String errorCode;
    private Integer statusCode;
    private String message;
    private T data;
    public static <T> ApiResponse<T> success(T object) {
        return ApiResponse.<T>builder()
                .data(object)
                .statusCode(HttpStatus.OK.value())
                .message("Success")
                .build();
    }
    public static <T> ApiResponse<T> error(String errorCode, Integer statusCode, String message) {
        return ApiResponse.<T>builder()
                .errorCode(errorCode)
                .statusCode(statusCode)
                .message(message)
                .build();
    }
}
