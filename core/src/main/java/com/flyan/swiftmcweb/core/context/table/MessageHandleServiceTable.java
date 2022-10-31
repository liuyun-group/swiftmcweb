package com.flyan.swiftmcweb.core.context.table;

import com.flyan.swiftmcweb.core.util.MessageCoreUtil;
import com.flyan.swiftmcweb.core.web.service.BaseMessageHandleService;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 消息处理服务表
 *
 * @author flyan
 */
@Data
public class MessageHandleServiceTable {

    private final Map<String, MessageHandleServiceInfo> messageHandleServiceMap = new HashMap<>();

    public MessageHandleServiceTable registerService(BaseMessageHandleService service, MessageHandleFuncTable handleFuncTable) {
        messageHandleServiceMap.put(MessageCoreUtil.getMessageHandlerServiceName(service),
                new MessageHandleServiceInfo(service, handleFuncTable));
        return this;
    }

    public boolean serviceExist(String service) {
        return messageHandleServiceMap.containsKey(service);
    }

    public MessageHandleServiceInfo getService(String service) {
        return messageHandleServiceMap.get(service);
    }

    @Override
    public String toString() {
        return messageHandleServiceMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, x -> x.getValue().getHandleFuncTable()))
                .toString();
    }

}
