package com.custom.Mockito.handler.impl.behaviour.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreMethodSignature {
    // todo : Behaviour 등록 할 때 Method 시그니처 검사 skip
}
