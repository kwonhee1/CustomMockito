package com.custom.Mockito.handler.impl.behaviour;

import com.custom.Mockito.handler.MethodHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Behaviour <RT> {

    public static final String CUSTOM_METHOD_NAME = "customMethod";

    private final Method customMethod = null;
    private final MethodHandler previousHandler = null;

    private Object instance;

    public Behaviour(){}

    protected RT originMethod(Object ...args) {
        return (RT) previousHandler.invoke(instance, args);
    }

    public Object invoke(Object instance, Object... args) {
        try {
            this.instance = instance;
            return customMethod.invoke(this, args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
