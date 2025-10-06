package com.streamnz.practisee.service.handler.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OutageHandlerType {
    /**
     * The type of outage handler.
     * @return
     */
    String value();
}
