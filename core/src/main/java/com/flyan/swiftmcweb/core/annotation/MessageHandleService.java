package com.flyan.swiftmcweb.core.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

/**
 * 消息处理服务注解，使用此注解的服务必须同时集成自 BaseMessageHandleService 才会被注册到消息处理者表中。
 *
 * @author flyan
 * @version 1.0
 * @date 10/10/22
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
@RequestMapping("/mipc")
public @interface MessageHandleService {

    @AliasFor(annotation = RestController.class, value = "value")
    String service() default "";

}
