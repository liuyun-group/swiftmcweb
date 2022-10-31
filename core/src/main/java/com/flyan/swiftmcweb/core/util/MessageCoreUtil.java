package com.flyan.swiftmcweb.core.util;

import com.flyan.swiftmcweb.core.annotation.MessageHandleService;
import com.flyan.swiftmcweb.core.web.service.BaseMessageHandleService;
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

    public static String getMessageHandlerServiceName(BaseMessageHandleService service) {
        var anno = service.getClass().getAnnotation(MessageHandleService.class);
        return anno != null ? anno.service() : null;
    }

}
