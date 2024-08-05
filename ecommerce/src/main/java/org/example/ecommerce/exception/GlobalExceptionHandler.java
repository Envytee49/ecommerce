package org.example.ecommerce.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.example.ecommerce.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleAppException(AppException exception) {
        log.error(exception.getMessage());
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
        log.error(message);
        return ApiResponse.builder()
                .errorCode(ErrorCode.INVALID_PARAMETER_TYPE.getCode())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleParameterNotValid(MethodArgumentNotValidException exception) {
        log.error(Objects.requireNonNull(exception.getFieldError()).getDefaultMessage());
        return ApiResponse.builder()
                .errorCode(ErrorCode.PARAMETER_NOT_VALID.getCode())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(Objects.requireNonNull(exception.getFieldError()).getDefaultMessage())
                .build();
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleConstraintViolation(ConstraintViolationException exception) {
        log.error(exception.getMessage());
        return ApiResponse.builder()
                .errorCode(ErrorCode.INVALID_PARAMETER_DATA.getCode())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleConstraintViolation(MissingServletRequestParameterException exception) {
        log.error(exception.getMessage());
        return ApiResponse.builder()
                .errorCode(ErrorCode.INVALID_PARAMETER_DATA.getCode())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(exception.getParameterName() + " is missing.")
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleNotReadableException(HttpMessageNotReadableException exception) {
        String message = exception.getRootCause() != null ? exception.getRootCause().getMessage() :
                ErrorCode.INVALID_DATA_FORMAT.getMessage();
        log.error(message);
        return ApiResponse.builder()
                .errorCode(ErrorCode.INVALID_DATA_FORMAT.getCode())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .build();
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleNoResourceFoundException(NoResourceFoundException exception) {
        log.error(exception.getMessage());
        return ApiResponse.builder()
                .errorCode(ErrorCode.NO_RESOURCE_FOUND.getCode())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(ErrorCode.NO_RESOURCE_FOUND.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleUnknownException(Exception exception) {
        exception.printStackTrace();
        return ApiResponse.builder()
                .errorCode(ErrorCode.UNKNOWN.getCode())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ErrorCode.UNKNOWN.getMessage())
                .build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleBadCredentialsException(BadCredentialsException exception) {
        return ApiResponse.builder()
                .errorCode(ErrorCode.BAD_CREDENTIALS.getCode())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ErrorCode.BAD_CREDENTIALS.getMessage())
                .build();
    }
}
