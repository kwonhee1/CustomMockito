package com.custom.Mockito;

import static net.bytebuddy.implementation.MethodDelegation.to;
import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.UUID;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.StubMethod;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class MockitoApplicationTests {

    TargetObject mockedObject = Mockito.mock(TargetObject.class);

	@Test
	void mockTest() {
         Mockito.when(mockedObject.returnOne()).thenReturn(2);

         assertEquals(2, mockedObject.returnOne());
         assertEquals(1, new TargetObject().returnOne());

//         System.out.println(mockedObject.getClass().getModule());
//         System.out.println(TargetObject.class.getModule());
//         System.out.println(new TargetObject().getClass().getModule());
//        System.out.println(Integer.class.getModule());
	}

    @Test
    public void mockStaticUUID() {
        UUID fixedUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        try (MockedStatic<UUID> uuidMock = mockStatic(UUID.class)) {
            uuidMock.when(UUID::randomUUID).thenReturn(fixedUUID);

//            UUID result = UUID.randomUUID();
//            assertEquals(fixedUUID, result);
        }
    }

    @Test
    public void make() throws Throwable {
        net.bytebuddy.agent.ByteBuddyAgent.install();

        Constructor<TargetObject> constructor = getConstructor(TargetObject.class);
        Object[] parameters = makeArguments(constructor);

        constructor.setAccessible(true);

        TargetObject mockedInstance = new ByteBuddy()
                .subclass(TargetObject.class)
                .constructor(any())
                .intercept(to(new Object() {
                    public void construct() throws Exception {
                        System.out.println("CALLING XTOR");
                    }
                })//.andThen(SuperMethodCall.INSTANCE)
                )
                .method(ElementMatchers.named("returnOne"))
                .intercept(FixedValue.value(3))
                .make()
                .load(TargetObject.class.getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                .getLoaded()
                .newInstance();

        assertEquals(3, mockedInstance.returnOne());
    }

    private <T> Constructor<T> getConstructor(Class<T> targetClass) {
        Constructor<?>[] constructors = targetClass.getDeclaredConstructors();
        Constructor selected = constructors[0];
        int selectedParameterCount = selected.getParameterCount();
        for(Constructor constructor : constructors) {
            if(selectedParameterCount > constructor.getParameterCount()) {
                ;
            }
        }

        return selected;
    }

    private Object[] makeArguments(Constructor targetConstructor) {
        Class<?>[] parameterTypes = targetConstructor.getParameterTypes();
        Object[] arguments = new Object[parameterTypes.length];

        for(int i = 0; i < parameterTypes.length; i++) {
            arguments[i] = makeStandardArgument(parameterTypes[i]);
        }

        return arguments;
    }

    private Object makeStandardArgument(Class<?> type) {
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

}
