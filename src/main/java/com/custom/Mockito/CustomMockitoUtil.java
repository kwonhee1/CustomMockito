package com.custom.Mockito;

import com.custom.Mockito.mock.MockMaker;
import com.custom.Mockito.handler.ClassHandler;
import com.custom.Mockito.handler.TotalHandler;
import com.custom.Mockito.handler.impl.origin.OriginHandlerFactory;
import com.custom.Mockito.handler.impl.value.ValueHandlerFactory;
import com.custom.Mockito.mock.bytebuddy.ByteBuddyMockMaker;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.bytebuddy.agent.ByteBuddyAgent;

public final class CustomMockitoUtil {

    private static final CustomMockitoUtil instance = new CustomMockitoUtil();
    private static final MockMaker MOCK_MAKER = new ByteBuddyMockMaker();

    private static final Map<Class, ClassHandler> mockMethodHandlerMap = new HashMap<>();

    private static final TotalHandler totalHandler = new TotalHandler(mockMethodHandlerMap);

    private CustomMockitoUtil() {
        // must run once!! do not run more once!!
        ByteBuddyAgent.install();
    }

    public static CustomMockitoUtil of() {
        return instance;
    }

    public static void mock(Class mockClass) {
        Class mockedClass = MOCK_MAKER.mock(mockClass);
        totalHandler.addClass(mockedClass);
        totalHandler.mockAllMethods(mockedClass, new ValueHandlerFactory());
    }

    public static void spy(Class mockClass) {
        Class mockedClass = MOCK_MAKER.mock(mockClass);
        totalHandler.addClass(mockedClass);
        totalHandler.mockAllMethods(mockClass, new OriginHandlerFactory());
    }

    public static void handle(Method method, Object handler) {
        totalHandler.updateHandler(method, handler);
    }

    public static void reset(Method method) {
        totalHandler.reset(method, new OriginHandlerFactory());
    }

    public static Method findMethod(Supplier supply) {
        totalHandler.startTrackMethod();

        supply.get();

        return totalHandler.getExcutedMethod();
    }

    public static When when(Supplier supply) {
        return new When(findMethod(supply));
    }

    public static class When {
        private Method method;

        When(Method method) {
            this.method = method;
        }

        public void handle(Object handler) {
            CustomMockitoUtil.handle(method, handler);
        }
        public void reset() {
            CustomMockitoUtil.reset(method);
        }
    }

}
