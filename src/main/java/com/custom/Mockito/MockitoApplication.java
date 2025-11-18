package com.custom.Mockito;

import com.custom.Mockito.handler.impl.behaviour.Behaviour;
import com.custom.Mockito.handler.impl.value.Value;

public class MockitoApplication {

	public static void main(String[] args) throws NoSuchMethodException {
        customMocking();
    }

    private static void customMocking() throws NoSuchMethodException {
        CustomMockitoUtil.mock(TargetObject.class);

        System.out.println(new TargetObject().returnInt());

        CustomMockitoUtil
                .when(()->new TargetObject().returnInt())
                .handle(new Value(4));

        System.out.println(new TargetObject().returnInt());

        CustomMockitoUtil
                .when(()->new TargetObject().returnInt())
                .handle(new Behaviour() {
                    public int returnInt() {
                        System.out.println("proxy!!");
                        return (int) originMethod();
                    }
                });

        CustomMockitoUtil
                .when(()->new TargetObject().returnInt()).reset();

        System.out.println(new TargetObject().returnInt());

    }

}
