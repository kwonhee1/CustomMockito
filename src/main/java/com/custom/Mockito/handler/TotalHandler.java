package com.custom.Mockito.handler;

import com.custom.Mockito.handler.exception.NotMockException;
import com.custom.Mockito.handler.factory.MethodHandlerFactory;
import com.custom.Mockito.util.DefaultValueUtil;
import java.lang.reflect.Method;
import java.util.Map;

public class TotalHandler {

    private static Map<Class, ClassHandler> mockMethodHandlerMap;
    private static ThreadLocal<Method> firstExcuteMethod = new ThreadLocal<>();

    public TotalHandler(Map<Class, ClassHandler> mockMethodHandlerMap) {
        TotalHandler.mockMethodHandlerMap = mockMethodHandlerMap;
        firstExcuteMethod.set(this.getClass().getDeclaredMethods()[0]); // init any method : set not null
    }

    public static Object handle(Method method, Object instance, Object... args) {
        if(firstExcuteMethod.get() == null) {
            firstExcuteMethod.set(method);
            return DefaultValueUtil.generate(method.getReturnType());
        }

        ClassHandler classHandler = mockMethodHandlerMap.get(method.getDeclaringClass());
        if (classHandler == null)
            throw new NotMockException("not mocked class " + method.getDeclaringClass().getName());

        return classHandler.handle(method, instance, args);
    }

    public void addClass(Class mockClass) {
        ClassHandler classHandler = new ClassHandler(mockClass);
        mockMethodHandlerMap.put(mockClass, classHandler);
    }

    public void mockAllMethods(Class mockClass, MethodHandlerFactory factory) {
        ClassHandler classHandler = mockMethodHandlerMap.get(mockClass);

        if(classHandler == null)
            throw new NotMockException("not mocked class " + mockClass.getName());

        classHandler.mockAllMethod(factory);
    }

    public void updateHandler(Method method, Object handler) {
        ClassHandler classHandler = mockMethodHandlerMap.get(method.getDeclaringClass());
        if (classHandler == null)
            throw new NotMockException("not mocked class " + method.getDeclaringClass().getName());

        classHandler.updateMethodHandler(method, handler);
    }

    public void startTrackMethod() {
        firstExcuteMethod.set(null);
    }

    public Method getExcutedMethod() {
        Method excutedMethod = firstExcuteMethod.get();
        firstExcuteMethod.set(this.getClass().getDeclaredMethods()[0]);
        return excutedMethod;
    }

    public void reset(Method method, MethodHandlerFactory factory) {
        mockMethodHandlerMap.get(method.getDeclaringClass()).resetMethodHandler(method, factory);
    }

}
