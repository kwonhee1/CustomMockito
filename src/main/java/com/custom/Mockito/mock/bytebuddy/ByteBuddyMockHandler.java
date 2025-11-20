package com.custom.Mockito.mock.bytebuddy;

import com.custom.Mockito.handler.TotalHandler;
import com.custom.Mockito.maker.OriginMethodRegistry;
import java.lang.reflect.Method;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

public class ByteBuddyMockHandler {

    @Advice.OnMethodEnter(skipOn = Advice.OnDefaultValue.class)
    public static boolean enter(
            @Advice.Origin Method method
    ) {
        if(OriginMethodRegistry.isOriginMethod(method))
            return true;
        return false;
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit(
            @Advice.Origin Method method,
            @Advice.AllArguments Object[] args,
            @Advice.Enter boolean isOrigin,
            @Advice.This Object instance,
            @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object returnValue
    ) {
        if(!isOrigin)
            returnValue = TotalHandler.handle(method, instance, args);
    }

}
