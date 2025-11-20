package com.custom.Mockito.handler.impl.behaviour;

import com.custom.Mockito.handler.MethodHandler;
import com.custom.Mockito.handler.exception.ArgumentMismatchException;
import com.custom.Mockito.handler.exception.FunctionalHandlerInitFailException;
import com.custom.Mockito.handler.exception.MethodMismatchException;
import com.custom.Mockito.handler.exception.NoCustomMethodException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BehaviourMethodHandler implements MethodHandler {

    private final Method originMethod;
    private Behaviour behaviour;

    public BehaviourMethodHandler(Method originMethod, Object behaviour, MethodHandler previousHandler) {
        this.originMethod = originMethod;
        initBehaviour(behaviour, previousHandler);
    }

    @Override
    public Object invoke(Object instance, Object[] args) {
        return behaviour.invoke(instance, args);
    }

    private void initBehaviour(Object behaviourObject, MethodHandler previousHandler) {
        Behaviour nextHandler = (Behaviour) behaviourObject;

        validateBehaviourHandler(nextHandler);

        setBeforeFunctionalHandler(nextHandler, previousHandler);

        this.behaviour = nextHandler;
    }

    private void validateBehaviourHandler(Behaviour behaviourHandler) {
        Method behaviourMethod;
        try {
            behaviourMethod = behaviourHandler.getClass().getDeclaredMethod(originMethod.getName(), originMethod.getParameterTypes());
            behaviourMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new MethodMismatchException(e.getMessage());
        }

        if(behaviourMethod.getReturnType() != originMethod.getReturnType())
            throw new MethodMismatchException(
                    "Method return type mismatch: expected '" + originMethod.getReturnType().getTypeName() +
                            "', but got '" + behaviourMethod.getReturnType().getTypeName() + "'."
            );

        if(behaviourMethod.getParameterTypes().length != originMethod.getParameterTypes().length)
            throw new ArgumentMismatchException(
                    "Argument length mismatch: expected " + originMethod.getParameterTypes().length +
                            " parameter(s), but got " + behaviourMethod.getParameterTypes().length + "."
            );

        Class[] newArgs = behaviourMethod.getParameterTypes();
        Class[] originArgs = originMethod.getParameterTypes();
        for(int i = 0; i < newArgs.length; i++) {
            if(newArgs[i] != originArgs[i])
                throw new ArgumentMismatchException(
                        "Argument mismatch at index " + i +
                                ": expected '" + originArgs[i] +
                                "', but got '" + newArgs[i] + "'."
                );
        }
    }

    private void setBeforeFunctionalHandler(
            Behaviour nextHandler,
            MethodHandler previousHandler
    ) {
        Method customMethod = null;
        try {
            customMethod = nextHandler.getClass().getMethod(originMethod.getName(), originMethod.getParameterTypes());
            customMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new NoCustomMethodException("No custom method initialized");
        }

        try {
            Field customMethodField = Behaviour.class.getDeclaredField(Behaviour.CUSTOM_METHOD_NAME);
            Field previousHandlerField = Behaviour.class.getDeclaredField("previousHandler");

            customMethodField.setAccessible(true);
            previousHandlerField.setAccessible(true);

            customMethodField.set(nextHandler, customMethod);
            previousHandlerField.set(nextHandler, previousHandler);
        }catch (NoSuchFieldException | IllegalAccessException e) {
            throw new FunctionalHandlerInitFailException(e.getClass().getName() + " : " + e.getMessage());
        }
    }

}
