package com.custom.Mockito.mock;

public interface MockMaker {
    Class mock(Class mockClass);
    boolean canMock(Class mockClass);
}
