package org.example.ecommerce.exception;

import jakarta.validation.ConstraintViolationException;
import org.example.ecommerce.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.format.DateTimeParseException;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> handleAppException(AppException exception) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(exception.getErrorCode().getCode());
        apiResponse.setMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> handleMismatchParametersType(MethodArgumentTypeMismatchException exception) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.INVALID_PARAMETER_TYPE.getCode());
        apiResponse.setMessage("The required type of " +
                exception.getPropertyName() +
                " is " +
                Objects.requireNonNull(exception.getRequiredType()).getSimpleName());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleParameterNotValid(MethodArgumentNotValidException exception) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.PARAMETER_NOT_VALID.getCode());
        apiResponse.setMessage(Objects.requireNonNull(exception.getFieldError()).getDefaultMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleConstraintViolation(ConstraintViolationException exception) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.INVALID_PARAMETER_DATA.getCode());
        apiResponse.setMessage(exception.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse> handleConstraintViolation(MissingServletRequestParameterException exception) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.INVALID_PARAMETER_DATA.getCode());
        apiResponse.setMessage(exception.getParameterName() + " is missing.");

        return ResponseEntity.badRequest().body(apiResponse);
    }
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse> handleNoResourceFoundException(NoResourceFoundException exception) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.NO_RESOURCE_FOUND.getCode());
        apiResponse.setMessage(ErrorCode.NO_RESOURCE_FOUND.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ApiResponse> handleDateTimeFormatException(DateTimeParseException exception) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.INVALID_DATE_FORMAT.getCode());
        apiResponse.setMessage(ErrorCode.INVALID_DATE_FORMAT.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleUnknownException(Exception exception) {
        exception.printStackTrace();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(StatusCode.UNKNOWN.getCode());
        apiResponse.setMessage("UNKNOWN ERROR.");

        return ResponseEntity.internalServerError().body(apiResponse);
    }
}
