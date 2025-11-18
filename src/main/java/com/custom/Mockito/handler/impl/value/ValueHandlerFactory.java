package com.custom.Mockito.handler.impl.value;

import com.custom.Mockito.handler.MethodHandler;
import com.custom.Mockito.handler.factory.MethodHandlerFactory;
import java.lang.reflect.Method;

public class ValueHandlerFactory implements MethodHandlerFactory {

    @Override
    public MethodHandler generate(Method method, Object handler, MethodHandler oldHandler) {
        return new ValueHandler(method, handler);
    }

    @Override
    public boolean validateHandler(Object handler) {
        return handler instanceof Value;
    }

}
