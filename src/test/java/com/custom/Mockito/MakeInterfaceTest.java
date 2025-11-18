//package com.custom.Mockito;
//
//import com.sun.jdi.InterfaceType;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import net.bytebuddy.ByteBuddy;
//import org.junit.jupiter.api.Test;
//
//public class MakeInterfaceTest {
//
//    Class<?> createdInterface = makeInterfaceOne(TargetObject.class.getDeclaredMethod("returnOne"));
//
//    public MakeInterfaceTest() throws NoSuchMethodException {
//    }
//
//    @Test
//    public void main() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        net.bytebuddy.agent.ByteBuddyAgent.install();
//
//        new MakeInterfaceTest().thenReturn((Class<? extends InterfaceType>) (Object) new createdInterface() {
//            public int invoke() {
//                return 5;
//            }
//        });
//    }
//
//    public Class<?> makeInterfaceOne(Method targetMethod) throws NoSuchMethodException {
//        Class<? extends InterfaceType> createdInterface = (Class<? extends InterfaceType>) new ByteBuddy()
//                .makeInterface()
//                .defineMethod(
//                        targetMethod.getName(),
//                        targetMethod.getReturnType()
//                )
//                .withParameters(targetMethod.getParameterTypes())
//                .withoutCode()
//                .make()
//                .load(targetMethod.getClass().getClassLoader())
//                .getLoaded();
//
//        return createdInterface;
//    }
//
//    public void thenReturn(Class<? extends InterfaceType> impl)
//            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        System.out.println(impl.getClass().getDeclaredMethod("invoke").invoke(impl));
//    }
//}
///*
//망했다 시발 도저히 불가능하다
//        다른 방법도 없어 보인다
//        도저히 람다 or interface으로 구현이 불가능하다
//        찍은 interface는 컴파일러시 도움을 받을 방법이 없다
//*/