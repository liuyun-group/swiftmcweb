package com.flyan.swiftmcweb.core.web.controller;


import com.flyan.swiftmcweb.core.pojo.Message;
import com.flyan.swiftmcweb.core.pojo.ResponseMessage;
import com.flyan.swiftmcweb.core.web.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 系统 - 消息控制器
 *
 * @apiNote 消息控制器，完成消息通信的处理
 * @author flyan
 * @version 1.0
 * @date 10/18/22
 */
@Slf4j
@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 发送一条消息，并接收响应的消息
     *
     * @apiNote 发送一条消息给某服务，请求某功能，并接收其响应的消息
     * @param message
     * @return
     */
    @PostMapping("/sendrec")
    public ResponseMessage<Object> sendrec(@Valid @RequestBody Message<Object> message) {
        return messageService.sendrec(message);
    }

}
