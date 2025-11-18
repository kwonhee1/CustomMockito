package com.custom.Mockito.handler.impl.behaviour;

import com.custom.Mockito.handler.MethodHandler;
import com.custom.Mockito.handler.factory.MethodHandlerFactory;
import java.lang.reflect.Method;

public class BehaviourHandlerFactory implements MethodHandlerFactory {

    @Override
    public MethodHandler generate(Method method, Object handler, MethodHandler oldHandler) {
        return new BehaviourMethodHandler(method, handler, oldHandler);
    }

    @Override
    public boolean validateHandler(Object handler) {
        return handler instanceof Behaviour;
    }
}
