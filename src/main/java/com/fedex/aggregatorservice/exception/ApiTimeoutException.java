package com.fedex.aggregatorservice.exception;

public class ApiTimeoutException extends RuntimeException {
    private String message;

    public ApiTimeoutException() {}

    public ApiTimeoutException(String msg)
    {
        super(msg);
        this.message = msg;
    }
}

