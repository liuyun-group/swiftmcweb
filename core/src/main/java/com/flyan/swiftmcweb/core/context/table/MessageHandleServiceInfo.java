package com.flyan.swiftmcweb.core.context.table;

import com.alibaba.fastjson2.annotation.JSONField;
import com.flyan.swiftmcweb.core.annotation.MessageHandleFunc;
import com.flyan.swiftmcweb.core.util.MessageCoreUtil;
import com.flyan.swiftmcweb.core.web.service.BaseMessageHandleService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息处理服务信息
 *
 * @author flyan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageHandleServiceInfo {

    /**
     * 消息处理服务
     */
    @JSONField(serialize = false, deserialize = false)
    private BaseMessageHandleService service;

    /**
     * 消息处理函数表，其包含当前处理服务里被 {@link MessageHandleFunc} 注解的所有功能函数。
     */
    private MessageHandleFuncTable handleFuncTable;

    @Override
    public String toString() {
        return "{\n" +
                "\tservice: " + MessageCoreUtil.getMessageHandlerServiceName(service) + ",\n" +
                "\thandleFuncTable: " + handleFuncTable + "\n" +
                '}';
    }
}