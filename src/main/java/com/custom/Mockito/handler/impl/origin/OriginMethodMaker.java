package com.custom.Mockito.handler.impl.origin;

import com.custom.Mockito.maker.Maker;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class OriginMethodMaker implements Maker {

    private final ConcurrentHashMap<Method, Boolean> inMemoryMaker = new ConcurrentHashMap<>();

    @Override
    public boolean isOriginMethod(Method method) {
        Boolean isOriginMethod = inMemoryMaker.get(method);

        if(isOriginMethod!=null && isOriginMethod) {
            inMemoryMaker.put(method, false);
            return true;
        }

        return false;
    }

    public void setIsOriginMethodTrue(Method method) {
        inMemoryMaker.put(method, true);
    }

}
