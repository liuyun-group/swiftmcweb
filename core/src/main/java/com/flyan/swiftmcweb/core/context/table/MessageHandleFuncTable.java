package com.flyan.swiftmcweb.core.context.table;

import com.flyan.swiftmcweb.core.annotation.MessageHandleFunc;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 消息处理函数表，其包含消息处理服务里被 {@link MessageHandleFunc} 注解的所有功能函数。
 *
 * @author flyan
 */
@Data
public class MessageHandleFuncTable {

    private final Map<String, Method> messageHandleFuncMap = new HashMap<>();

    public MessageHandleFuncTable registerHandleFunc(String messtype, Method func) {
        messageHandleFuncMap.put(messtype, func);
        return this;
    }

    public boolean handleFuncExist(String messtype) {
        return messageHandleFuncMap.containsKey(messtype);
    }

    public Method getHandleFuncName(String messtype) {
        return messageHandleFuncMap.get(messtype);
    }

    @Override
    public String toString() {
        return messageHandleFuncMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, x -> x.getValue().getName()))
                .toString();
    }

}
