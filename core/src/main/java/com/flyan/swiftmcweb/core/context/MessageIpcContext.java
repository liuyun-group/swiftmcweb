package com.flyan.swiftmcweb.core.context;

import cn.hutool.core.map.MapUtil;
import com.flyan.swiftmcweb.core.context.table.MessageHandleServiceTable;
import com.flyan.swiftmcweb.core.pojo.ServiceFuncInfo;

import java.util.HashSet;
import java.util.Set;

/**
 * 消息通信上下文
 *
 * @author flyan
 * @version 1.0
 * @date 10/20/22
 */
public class MessageIpcContext {

    /**
     * 单例
     */
    private static final MessageIpcContext CONTEXT = new MessageIpcContext();
    public static MessageIpcContext context() {
        return CONTEXT;
    }

    /**
     * 消息处理服务表
     */
    private final static MessageHandleServiceTable messageHandleServiceTable = new MessageHandleServiceTable();

    /**
     * 服务功能校验白名单
     */
    private final static Set<ServiceFuncInfo> authWhitelist = new HashSet<>();

    public MessageHandleServiceTable getMessageHandleServiceTable() {
        return messageHandleServiceTable;
    }

    public void addAuthWhiteListEntry(String service, String messtype) {
        authWhitelist.add(new ServiceFuncInfo().setService(service).setMesstype(messtype));
    }

    public boolean inAuthWhiteList(String service, String messtype) {
        return authWhitelist.contains(new ServiceFuncInfo().setService(service).setMesstype(messtype));
    }

    @Override
    public String toString() {
        return MapUtil.builder()
                .put("messageHandleServiceTable", messageHandleServiceTable)
                .put("authWhitelist", authWhitelist)
                .build().toString();
    }

}
