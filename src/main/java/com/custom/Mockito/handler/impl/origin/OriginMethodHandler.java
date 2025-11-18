package com.custom.Mockito.handler.impl.origin;

import com.custom.Mockito.handler.MethodHandler;
import com.custom.Mockito.maker.TotalMaker;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OriginMethodHandler implements MethodHandler {

    private final Method originMethod;
    private final static OriginMethodMaker maker = new OriginMethodMaker();

    public OriginMethodHandler(Method originMethod) {
        this.originMethod = originMethod;

        originMethod.setAccessible(true);
    }

    static {
        TotalMaker.of().addMaker(maker);
    }

    @Override
    public Object invoke(Object instance, Object[] args) {
        maker.setIsOriginMethodTrue(originMethod);
        try {
            return originMethod.invoke(instance, args);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
