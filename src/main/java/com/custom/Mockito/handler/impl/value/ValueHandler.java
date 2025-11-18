package com.custom.Mockito.handler.impl.value;

import com.custom.Mockito.handler.MethodHandler;
import com.custom.Mockito.handler.exception.MethodMismatchException;
import com.custom.Mockito.util.DefaultValueUtil;
import java.lang.reflect.Method;

public class ValueHandler implements MethodHandler {

    private Object returnValue;

    public ValueHandler(Method method, Object handler) {
        if(handler == null) {
            this.returnValue = DefaultValueUtil.generate(method.getReturnType());
            return;
        }

        Object returnValue = ((Value)handler).getValue();

        if(!returnValue.getClass().equals(DefaultValueUtil.toWrapper(method.getReturnType())))
            throw new MethodMismatchException(
                    "Method return type mismatch: expected '" + method.getReturnType().getTypeName() +
                            "', but got '" + returnValue.getClass().getTypeName() + "'."
            );

        this.returnValue = returnValue;
    }

    @Override
    public Object invoke(Object instance, Object[] args) {
        return returnValue;
    }

}
