module Mockito.main {
    requires net.bytebuddy;
    requires org.mockito;
    requires net.bytebuddy.agent;
    requires Mockito.main;
    requires java.instrument;
}