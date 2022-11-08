package com.liuyun.swiftmcweb.core.config;

import com.liuyun.swiftmcweb.core.context.MessageIpcContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息通信配置
 *
 * @author flyan
 * @version 1.0
 * @date 10/20/22
 */
@Configuration
public class MessageIpcConfig {

    /**
     * 消息通信上下文
     * @return
     */
    @Bean
    public MessageIpcContext messageIpcContext() {
        return MessageIpcContext.context();
    }

}
