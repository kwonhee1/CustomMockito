package com.custom.Mockito.mock;

import com.custom.Mockito.handler.TotalHandler;
import com.custom.Mockito.maker.TotalMaker;
import java.lang.reflect.Method;

public interface MockHandler {

    Object originMethod(Object... args);

    default Object handle(
            Method method,
            Object[] args,
            Object instance
    ) {
        if(TotalMaker.isOriginMethod(method))
            return originMethod(args);
        return  TotalHandler.handle(method, instance, args);
    }

}
