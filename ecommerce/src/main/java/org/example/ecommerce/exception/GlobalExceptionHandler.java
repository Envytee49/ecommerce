package org.example.ecommerce.exception;

import jakarta.validation.ConstraintViolationException;
import org.example.ecommerce.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleAppException(AppException exception) {
        return ApiResponse.builder()
                .errorCode(exception.getErrorCode().getCode())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleMismatchParametersType(MethodArgumentTypeMismatchException exception) {
        String message = "The required type of " +
                exception.getPropertyName() +
                " is " +
                Objects.requireNonNull(exception.getRequiredType()).getSimpleName();
        return ApiResponse.builder()
                .errorCode(ErrorCode.INVALID_PARAMETER_TYPE.getCode())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleParameterNotValid(MethodArgumentNotValidException exception) {
        return ApiResponse.builder()
                .errorCode(ErrorCode.PARAMETER_NOT_VALID.getCode())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(Objects.requireNonNull(exception.getFieldError()).getDefaultMessage())
                .build();
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleConstraintViolation(ConstraintViolationException exception) {
        return ApiResponse.builder()
                .errorCode(ErrorCode.INVALID_PARAMETER_DATA.getCode())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleConstraintViolation(MissingServletRequestParameterException exception) {
        return ApiResponse.builder()
                .errorCode(ErrorCode.INVALID_PARAMETER_DATA.getCode())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(exception.getParameterName() + " is missing.")
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleNotReadableException(HttpMessageNotReadableException exception) {
        return ApiResponse.builder()
                .errorCode(ErrorCode.INVALID_DATA_FORMAT.getCode())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ErrorCode.INVALID_DATA_FORMAT.getMessage())
                .build();
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleNoResourceFoundException(NoResourceFoundException exception) {
        return ApiResponse.builder()
                .errorCode(ErrorCode.NO_RESOURCE_FOUND.getCode())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(ErrorCode.NO_RESOURCE_FOUND.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleUnknownException(Exception exception) {
        return ApiResponse.builder()
                .errorCode(ErrorCode.UNKNOWN.getCode())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ErrorCode.UNKNOWN.getMessage())
                .build();
    }
}
