package com.custom.Mockito;

import com.custom.Mockito.handler.factory.MethodHandlerFactory;
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

/**
 * <p> Custom Mockito </p>
 *
 * The Custom Mockito library
 * Mock Class and set method dynamic!
 *
 * <p> 1. mocking class </p>
 * <pre class="code"><code class="java">
 *     CustomMockitoUtil.mock(TargetObject.class); // mock TargetObject.class
 *     // CustomMockito does not return an instance; it mocks the class !
 *
 *     CustomMockitoUtil.spy(TargetObject.class); // mock TargetObject.class
 *     // Spy does not erase the original methods. If you do not set a handler, the original method will run
 * </code></pre>
 *
 * <p> 2. set method </p>
 * <pre class="code"><code class="java">
 *     CustomMockitoUtil
 *         .when(()->new TargetObject().returnInt()) // find the method
 *         .handle(new Value(4)); // set method
 *
 *     System.out.println(new TargetObject().returnInt()); // "4"
 * </code></pre>
 *
 * <p> 3. set method dynamic! </p>
 * you can set method dynamic!
 * use Behaviour Handler to redefine origin method
 * <pre class="code"><code class="java">
 *     CustomMockitoUtil
 *         .when(()->new TargetObject().returnSum(0,0))
 *         .handle(new Behaviour() {
 *             public Integer returnSum(int a, int b) { // TargetObject::returnSum method signature
 *                 System.out.println("argument : " + a +", and " + b);
 *                 return a * b;
 *             }
 *         });
 *
 *     System.out.println(new TargetObject().returnSum(3,4)); // "argument : 3, and 4 \n 12"
 * </code></pre>
 * With Behaviour, you must redefine a method with the same signature as the original
 *
 * <p> 3. use Behaviour with originMethod </p>
 * when mock a class with spy, origin methods not erased
 * with Behaviour Handler you can use the origin method
 * <pre class="code"><code class="java">
 *     CustomMockitoUtil.spy(TargetObject.class);
 *
 *     CustomMockitoUtil
 *         .when(()->new TargetObject().returnSum(0,0)) // input any value
 *         .handle(new Behaviour() {
 *             public Integer returnSum(int a, int b) { // TargetObject::returnSum method signiture
 *                 System.out.println("argument : " + a +", and " + b);
 *                 return (Integer) originMethod(a * b, b - a); // return a + b; -> return 2 + 1;
 *             }
 *         });
 *
 *     System.out.println(new TargetObject().returnSum(1,2)); // "argument : 1, and 2 \n 1"
 *
 *     // you can mocking a method even if it is already mocked
 *     CustomMockitoUtil
 *         .when(()->new TargetObject().returnSum(0,0))
 *         .handle(new Behaviour() {
 *             public Integer returnSum(int a, int b) {
 *                 System.out.println("switch argument");
 *                 return (Integer) originMethod(b, a);
 *             }
 *         });
 *
 *     System.out.println(new TargetObject().returnSum(1,2)); // "switch argument \n argument : 2, and 1 \n 3"
 * </code></pre>
 *
 * <p> 4. reset method </p>
 * you can reset the mocked method to its origin method
 * <pre class="code"><code class="java">
 *     CustomMockitoUtil
 *         .when(()->new TargetObject().returnSum(0,0)).reset();
 *
 *     System.out.println(new TargetObject().returnSum(1, 2)); // "3"
 * </code></pre>
 *
 * <p> 5. implement capture with Behaviour </p>
 * <pre class="code"><code class="java">
 *     public static int captureA;
 *     public static int captureB;
 *
 *     CustomMockitoUtil.spy(TargetObject.class);
 *
 *     CustomMockitoUtil
 *         .when(()->new TargetObject().returnSum(0,0))
 *         .handle(new Behaviour() {
 *             public Integer returnSum(int a, int b) {
 *                 captureA = a; // capture A
 *                 captureB = b; // capture B
 *                 return (Integer) originMethod(a * b, a- b);
 *             }
 *         });
 *
 *     new TargetObject().returnSum(1, 2);
 *     System.out.println(captureA + ", " +  captureB); // 1, 2
 * </code></pre>
 */

public class CustomMockitoUtil {

    private static MockMaker MOCK_MAKER = new ByteBuddyMockMaker();

    private static final Map<Class, ClassHandler> mockMethodHandlerMap = new HashMap<>();

    private static final TotalHandler totalHandler = new TotalHandler(mockMethodHandlerMap);

    static {
        // must run once!! do not run more once!!
        ByteBuddyAgent.install();
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

    public static void addCustomMethodHandler(MethodHandlerFactory methodHandlerFactory) {
        MethodHandlerFactory.addFactory(methodHandlerFactory);
    }

    public static void setMockMaker(MockMaker inputMockMaker) {
        CustomMockitoUtil.MOCK_MAKER = inputMockMaker;
    }

}
