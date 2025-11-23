# Custom Mockito

The Custom Mockito library

## This project started from the Woo Pre-Course.   
>Custom Mockito implementation enhances dynamic mocking beyond Mockito’s limitations.    
>Custom Mockito’s Dynamic Mocking aims to provide flexible mocking,  
>  helping users implement their own Mockito capture functionality.    

[Analysis Mockito velog](https://velog.io/@lkh8033/Mockito-%EC%9D%B4%ED%95%B4%ED%95%98%EA%B8%B0-%EA%B0%84%EB%8B%A8%EC%86%8C%EA%B0%9C)  
[Custom Mockito velog](https://velog.io/@lkh8033/Custom-Mockito-%EB%A7%8C%EB%93%A4%EA%B8%B0)

## 1. mocking class

```
    CustomMockitoUtil.mock(TargetObject.class); // mock TargetObject.class
    // CustomMockito does not return an instance; it mocks the class !

    CustomMockitoUtil.spy(TargetObject.class); // mock TargetObject.class
    // Spy does not erase the original methods. If you do not set a handler, the original method will run
```

## 2. set method

```
    CustomMockitoUtil
        .when(()->new TargetObject().returnInt()) // find the method 
        .handle(new Value(4)); // set method

    System.out.println(new TargetObject().returnInt()); // "4"
```

## 3. set method Activity! 
you can set method Activity!
use Behaviour Handler to redefine origin method

```
    CustomMockitoUtil
        .when(()->new TargetObject().returnSum(0,0))
        .handle(new Behaviour() {
            public Integer returnSum(int a, int b) { // TargetObject::returnSum method signature
                System.out.println("argument : " + a +", and " + b);
                return a * b;
            }
        });

    System.out.println(new TargetObject().returnSum(3,4)); // "argument : 3, and 4 \n 12"
```

With Behaviour, you must redefine a method with the same signature as the original

## 3. use Behaviour with originMethod
when mock a class with spy, origin methods not erased
with Behaviour Handler you can use the origin method

```
    CustomMockitoUtil.spy(TargetObject.class);

    CustomMockitoUtil
        .when(()->new TargetObject().returnSum(0,0)) // input any value
        .handle(new Behaviour() {
            public Integer returnSum(int a, int b) { // TargetObject::returnSum method signiture
                System.out.println("argument : " + a +", and " + b);
                return (Integer) originMethod(a * b, b - a); // return a + b; -> return 2 + 1;
            }
        });

    System.out.println(new TargetObject().returnSum(1,2)); // "argument : 1, and 2 \n 1"

    // you can mocking a method even if it is already mocked
    CustomMockitoUtil
        .when(()->new TargetObject().returnSum(0,0))
        .handle(new Behaviour() {
            public Integer returnSum(int a, int b) {
                System.out.println("switch argument");
                return (Integer) originMethod(b, a);
            }
        });
    
    System.out.println(new TargetObject().returnSum(1,2)); // "switch argument \n argument : 2, and 1 \n 3"
```

## 4. reset method 
you can reset the mocked method to its origin method

```
    CustomMockitoUtil
        .when(()->new TargetObject().returnSum(0,0)).reset();

    System.out.println(new TargetObject().returnSum(1, 2)); // "3"
</code></pre>

## 5. implement capture with Behaviour
<pre class="code"><code class="java">
    public static int captureA;
    public static int captureB;
    
    CustomMockitoUtil.spy(TargetObject.class);

    CustomMockitoUtil
        .when(()->new TargetObject().returnSum(0,0))
        .handle(new Behaviour() {
            public Integer returnSum(int a, int b) {
                captureA = a; // capture A
                captureB = b; // capture B
                return (Integer) originMethod(a * b, a- b);
            }
        });

    new TargetObject().returnSum(1, 2);
    System.out.println(captureA + ", " +  captureB); // 1, 2
```
