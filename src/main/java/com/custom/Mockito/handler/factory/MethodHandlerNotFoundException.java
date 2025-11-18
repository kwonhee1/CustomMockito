package com.custom.Mockito.handler.factory;

public class MethodHandlerNotFoundException extends IllegalArgumentException {
    public MethodHandlerNotFoundException(String message) {
        super(message);
    }
}
