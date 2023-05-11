package com.liuyun.swiftmcweb.core.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 消息处理管理器注解，管理器编写主要业务代码，提供给消息处理服务，
 * 同时各个服务之间通过管理器进行内部功能调用。
 *
 * @author flyan
 * @version 1.0
 * @date 10/27/22
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MessageHandleManager {

    @AliasFor(annotation = Component.class)
    String value() default "";

    String name() default "";

}
