package com.custom.Mockito.handler;

import com.custom.Mockito.handler.exception.NotMockException;
import com.custom.Mockito.handler.factory.MethodHandlerFactory;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ClassHandler {

    private final Class tergetClass;
    private final Map<Method, MethodHandler> methodHandlerMap = new HashMap<>();

    public ClassHandler(Class tergetClass) {
        this.tergetClass = tergetClass;
    }

    public void mockAllMethod(MethodHandlerFactory factory) {
        for(Method method : tergetClass.getMethods()) {
            if(method.getDeclaringClass().equals(tergetClass))
                methodHandlerMap.put(method, factory.generate(method, null, methodHandlerMap.get(method)));
        }
    }

    public Object handle(Method method, Object instance, Object[] args) {
        MethodHandler targetMethodHandler = methodHandlerMap.get(method);

        if(targetMethodHandler == null)
            throw new NotMockException("not mock method " + tergetClass.getName());

        return targetMethodHandler.invoke(instance, args);
    }

    public void updateMethodHandler(Method method, Object handler) {
        MethodHandler oldHandler = methodHandlerMap.get(method);
        methodHandlerMap.put(method, MethodHandlerFactory.generateByHandler(method, handler, oldHandler));
    }

    public void resetMethodHandler(Method method, MethodHandlerFactory factory) {
        methodHandlerMap.put(method, factory.generate(method, null, methodHandlerMap.get(method)));
    }

}
