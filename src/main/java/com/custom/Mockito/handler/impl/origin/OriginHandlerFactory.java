package com.custom.Mockito.handler.impl.origin;

import com.custom.Mockito.handler.MethodHandler;
import com.custom.Mockito.handler.factory.MethodHandlerFactory;
import java.lang.reflect.Method;

public class OriginHandlerFactory implements MethodHandlerFactory {

    @Override
    public MethodHandler generate(Method method, Object handler, MethodHandler oldHandler) {
        return new OriginMethodHandler(method);
    }

    @Override
    public boolean validateHandler(Object handler) {
        return false;
    }
}
