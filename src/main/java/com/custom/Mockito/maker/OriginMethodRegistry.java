package com.custom.Mockito.maker;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class OriginMethodRegistry {

    protected static final OriginMethodRegistry instance = new OriginMethodRegistry();

    protected final ThreadLocal<Set<Method>> originMethodSet = new ThreadLocal<>();

    protected OriginMethodRegistry() {
        originMethodSet.set(new HashSet<>());
    }

    public static OriginMethodRegistry of() {
        return instance;
    }

    public static void setOriginMethod(Method method) {
        instance.originMethodSet.get().add(method);
    }

    public static boolean isOriginMethod(Method method) {
        boolean isOriginMethod = false;
        isOriginMethod = instance.originMethodSet.get().contains(method);

        if(isOriginMethod)
            instance.originMethodSet.get().remove(method);

        return isOriginMethod;
    }

}
