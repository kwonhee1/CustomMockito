package com.custom.Mockito;

public class TargetObject {

    public TargetObject() {
        System.out.println("TargetObject() :: constructor");
    }

    public Integer returnOne() {
        System.out.println("TargetObject() :: returnOne");
        return 1;
    }

    public Integer returnTwo() {
        System.out.println("TargetObject() :: returnTwo");
        return 2;
    }

    public int returnInt() {
        System.out.println("TargetObject() :: returnInt");
        return 1;
    }
    public Integer returnThree() {
        return 3;
    }

    public Integer throwException() {
        throw new RuntimeException("TargetObject() :: throwException");
    }

    public Integer returnSum(int a, int b) {
        return a + b;
    }

}
