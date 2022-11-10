package com.liuyun.swiftmcweb.core.context.table;

import com.liuyun.swiftmcweb.core.exception.SwiftmcwebException;
import com.liuyun.swiftmcweb.core.web.service.IMessageHandleService;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.liuyun.swiftmcweb.core.util.MessageCoreUtil.getMessageHandlerServiceName;

/**
 * 消息处理服务表
 *
 * @author flyan
 */
@Data
public class MessageHandleServiceTable {

    private final Map<String, MessageHandleServiceInfo> messageHandleServiceMap = new HashMap<>();

    public MessageHandleServiceTable registerService(IMessageHandleService service, MessageHandleFuncTable handleFuncTable) {
        var serviceName = getMessageHandlerServiceName(service);
        if (messageHandleServiceMap.containsKey(serviceName)) {
            throw new SwiftmcwebException("Duplicate service: " + serviceName);
        }
        messageHandleServiceMap.put(serviceName, new MessageHandleServiceInfo(service, handleFuncTable));
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
