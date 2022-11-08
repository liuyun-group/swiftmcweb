/**
 * swiftmcweb core.
 *
 * @author flyan
 * @version 1.0
 * @date 10/27/22
 */
open module com.flyan.swiftmcweb.core {
    requires transitive jdk.unsupported;
    requires transitive java.validation;

    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.context;
    requires spring.core;
    requires spring.security.config;
    requires spring.security.web;
    requires spring.web;
    requires spring.beans;
    requires spring.boot.actuator;
    requires spring.boot.actuator.autoconfigure;
    requires spring.webmvc;
    requires spring.security.core;
    requires org.apache.tomcat.embed.core;

    requires hutool.all;
    requires com.google.common;
    requires com.fasterxml.jackson.annotation;
    requires com.alibaba.fastjson2;
    requires org.slf4j;
    requires lombok;



    exports com.liuyun.swiftmcweb.core.annotation;
    exports com.liuyun.swiftmcweb.core.pojo;
    exports com.liuyun.swiftmcweb.core.exception;
    exports com.liuyun.swiftmcweb.core.exception.enums;
    exports com.liuyun.swiftmcweb.core.framework.security.core.enums;

    exports com.liuyun.swiftmcweb.core.web;
    exports com.liuyun.swiftmcweb.core.web.manager;
    exports com.liuyun.swiftmcweb.core.web.service;

    exports com.liuyun.swiftmcweb.core.util;
    exports com.liuyun.swiftmcweb.core.util.json;
    exports com.liuyun.swiftmcweb.core.util.jwt;
    exports com.liuyun.swiftmcweb.core.util.servlet;
    exports com.liuyun.swiftmcweb.core.util.time;
    exports com.liuyun.swiftmcweb.core.util.exception;
    exports com.liuyun.swiftmcweb.core.util.validate;
}