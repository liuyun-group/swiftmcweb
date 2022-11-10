package com.liuyun.swiftmcweb.core.util;

import com.liuyun.swiftmcweb.core.annotation.MessageHandleService;
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
        var anno = service.getClass().getAnnotation(MessageHandleService.class);
        return anno != null ? anno.service() : null;
    }

}
