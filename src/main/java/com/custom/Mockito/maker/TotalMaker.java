package com.custom.Mockito.maker;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TotalMaker {

    private static final TotalMaker instance = new TotalMaker();

    private final List<Maker> makerList = new ArrayList<>();

    protected TotalMaker() {}

    public static TotalMaker of() {
        return instance;
    }

    public void addMaker(Maker maker) {
        instance.makerList.add(maker);
    }

    public static boolean isOriginMethod(Method method) {
        boolean result = false;
        for(Maker maker : instance.makerList) {
            if(maker.isOriginMethod(method))
                result = true;
        }

        return result;
    }

}
