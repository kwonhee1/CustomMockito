module Mockito.main {
    requires net.bytebuddy;
    requires net.bytebuddy.agent;
    requires Mockito.main;

    exports com.custom.Mockito.handler.factory;
    exports com.custom.Mockito.handler.impl.value;
    exports com.custom.Mockito.handler.impl.behaviour;
    exports com.custom.Mockito.handler.impl.behaviour.annotation;
    exports com.custom.Mockito.handler;

    exports com.custom.Mockito.maker;

    exports com.custom.Mockito.mock;

    exports com.custom.Mockito.main;
}