package com.liuyun.swiftmcweb.core.annotation;

import java.lang.annotation.*;

/**
 * 此注解用于标注消息处理服务里的处理函数，
 *
 * @author flyan
 * @version 1.0
 * @date 10/20/22
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageHandleFunc {

    /**
     * 消息类型
     *
     * @return
     */
    String messtype() default "";

    /**
     * 是否需要登录校验，默认需要
     *
     * @return
     */
    boolean auth() default true;

}
