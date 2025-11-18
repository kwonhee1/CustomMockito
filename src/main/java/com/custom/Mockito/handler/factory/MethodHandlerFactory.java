package com.custom.Mockito.handler.factory;

import com.custom.Mockito.handler.MethodHandler;
import com.custom.Mockito.handler.impl.behaviour.BehaviourHandlerFactory;
import com.custom.Mockito.handler.impl.origin.OriginHandlerFactory;
import com.custom.Mockito.handler.impl.value.ValueHandlerFactory;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public interface MethodHandlerFactory {

    static List<MethodHandlerFactory> list = List.of(new OriginHandlerFactory(), new ValueHandlerFactory(), new BehaviourHandlerFactory());

    MethodHandler generate(Method method, Object handler, MethodHandler oldHandler);
    boolean validateHandler(Object handler);

    static MethodHandler generateByHandler(Method method, Object handler, MethodHandler previousHandler) {
        for(MethodHandlerFactory factory : list) {
            if(factory.validateHandler(handler))
                return factory.generate(method, handler, previousHandler);
        }

        throw new MethodHandlerNotFoundException("not found handler factory " + handler);
    }

    static void addFactory(MethodHandlerFactory factory) {
        list.add(factory);
    }

}
