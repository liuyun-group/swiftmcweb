package com.liuyun.swiftmcweb.core.annotation;

import java.lang.annotation.*;

/**
 * <p>
 *     原生 api，用于注在 Controller 的接口方法上，指明该接口是一个原生 api 接口，
 *     若是配置属性 auth=false 则该接口是一个完全开放的 api 接口，不会被认证，请
 *     谨慎使用。
 * </p>
 *
 * @author flyan
 * @version 1.0
 * @date 11/7/22
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OriginApi {

    /**
     * 是否需要登录校验，默认需要
     *
     * @return
     */
    boolean auth() default true;

}
