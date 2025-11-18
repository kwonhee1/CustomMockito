package com.custom.Mockito.util;

import java.util.Map;

public class DefaultValueUtil {

    private static final Map<Class<?>, Class<?>> primitiveWrapperMap = Map.of(
            int.class, Integer.class,
            long.class, Long.class,
            double.class, Double.class,
            float.class, Float.class,
            char.class, Character.class,
            short.class, Short.class,
            byte.class, Byte.class,
            boolean.class, Boolean.class
    );

    public static Object generate(Class type) {
        if (type == boolean.class)
            return false;
        if (type == byte.class)
            return (byte) 0;
        if (type == short.class)
            return (short) 0;
        if (type == char.class)
            return (char) 0;
        if (type == int.class)
            return 0;
        if (type == long.class)
            return 0L;
        if (type == float.class)
            return 0f;
        if (type == double.class)
            return 0d;

        return type.cast(null);
    }

    public static Class<?> toWrapper(Class<?> primitiveType) {
        return primitiveWrapperMap.getOrDefault(primitiveType, primitiveType);
    }

}
