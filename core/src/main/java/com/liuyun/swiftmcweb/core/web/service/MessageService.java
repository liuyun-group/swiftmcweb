package com.liuyun.swiftmcweb.core.web.service;

import com.liuyun.swiftmcweb.core.pojo.ResponseMessage;
import com.liuyun.swiftmcweb.core.pojo.Message;

/**
 * 消息服务 提供消息通信服务
 *
 * @author flyan
 * @version 1.0
 * @date 10/18/22
 */
public interface MessageService extends BaseMessageHandleService {

    /**
     * 发送一条消息到某个服务请求一个功能服务，返回一个包含处理结果的消息对象
     *
     * @param message
     * @return
     * @param <S>
     */
    ResponseMessage<Object> sendrec(Message<Object> message);

}
