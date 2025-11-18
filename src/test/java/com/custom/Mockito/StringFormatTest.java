package com.custom.Mockito;

import org.mockito.Mockito;

public class StringFormatTest {
    public void main() {
        TargetObject mockedInstance = Mockito.mock(TargetObject.class);
        TargetObject newInstance = new TargetObject();

        System.out.println(mockedInstance.getClass().getModule());
        System.out.println(newInstance.getClass().getModule());

        System.out.println(mockedInstance.getClass());
        System.out.println(newInstance.getClass());

        System.out.println(mockedInstance.returnOne());
        System.out.println(newInstance.returnOne());
    }
}
