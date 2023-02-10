package com.fedex.aggregatorservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ApiTimeoutException.class)
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public @ResponseBody ErrorResponse handleException(ApiTimeoutException ex) {
        return new ErrorResponse(HttpStatus.REQUEST_TIMEOUT.value(), ex.getMessage());
    }

}
