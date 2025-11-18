package com.custom.Mockito.capture;

import com.custom.Mockito.handler.ClassHandler;
import com.custom.Mockito.CustomMockitoUtil;
import com.custom.Mockito.handler.impl.behaviour.Behaviour;
import java.lang.reflect.Method;

public class Capture {

    private static ThreadLocal<Boolean> maker = new ThreadLocal<>();

    // private static final Capture INSTANCE = new Capture();

    public Capture() {
        maker.set(false);
        captureInit();
    }

    public void captureInit() {
        CustomMockitoUtil.spy(ClassHandler.class);
        Method classHandlerMethod;
        try {
            classHandlerMethod = ClassHandler.class.getDeclaredMethod("handle", Method.class, Object.class, Object[].class);
        } catch (NoSuchMethodException e) {
            throw new CaptureInitFailException("not found handle method " + e.getMessage());
        }

        CustomMockitoUtil.handle(classHandlerMethod, new Behaviour() {
            public Object handle(Method method, Object instance, Object[] args) {
                if(Capture.isCapture())
                    return method;

                return originMethod(args);
            }
        });
    }

    public static boolean isCapture() {
        boolean isCapture = maker.get();

        if(isCapture)
            maker.set(false);

        return isCapture;
    }

    public static void startCapture() {
        maker.set(true);
    }

    public static void endCapture() {
        maker.set(false);
    }

}
