package com.liuyun.swiftmcweb.core.util;

import com.liuyun.swiftmcweb.core.annotation.MessageHandleService;
import com.liuyun.swiftmcweb.core.exception.SwiftmcwebException;
import com.liuyun.swiftmcweb.core.web.service.IMessageHandleService;
import lombok.experimental.UtilityClass;

/**
 * 消息通信核心工具类
 *
 * @author flyan
 * @version 1.0
 * @date 10/20/22
 */
@UtilityClass
public class MessageCoreUtil {

    public static String getMessageHandlerServiceName(IMessageHandleService service) {
        var anno = getOriginClass(service).getAnnotation(MessageHandleService.class);
        return anno != null ? anno.service() : null;
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getOriginClass(T obj) {
        Class<?> clazz = obj.getClass();
        String name = clazz.getName();
        if (name.contains("$$EnhancerBySpringCGLIB") || name.contains("$$5770166e")) {
            name = name.substring(0, name.indexOf("$$"));
        }
        try {
            clazz = Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new SwiftmcwebException("MessageCoreUtil::getOriginClass failed, obj: " + obj);
        }
        return (Class<T>) clazz;
    }

}
