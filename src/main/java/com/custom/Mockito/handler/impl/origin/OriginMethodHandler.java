package com.custom.Mockito.handler.impl.origin;

import com.custom.Mockito.handler.MethodHandler;
import com.custom.Mockito.maker.OriginMethodRegistry;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OriginMethodHandler implements MethodHandler {

    private final Method originMethod;

    public OriginMethodHandler(Method originMethod) {
        this.originMethod = originMethod;

        originMethod.setAccessible(true);
    }

    @Override
    public Object invoke(Object instance, Object[] args) {
        OriginMethodRegistry.setOriginMethod(originMethod);
        try {
            return originMethod.invoke(instance, args);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        } finally {
            OriginMethodRegistry.isOriginMethod(originMethod);
        }
    }

}
