package com.liuyun.swiftmcweb.core.listener;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import com.liuyun.swiftmcweb.core.annotation.MessageHandleFunc;
import com.liuyun.swiftmcweb.core.annotation.MessageHandleService;
import com.liuyun.swiftmcweb.core.context.MessageIpcContext;
import com.liuyun.swiftmcweb.core.context.table.MessageHandleFuncTable;
import com.liuyun.swiftmcweb.core.util.MessageCoreUtil;
import com.liuyun.swiftmcweb.core.web.service.IMessageHandleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 自动注册由 {@link MessageHandleService} 注解的消息处理服务以及消息处理服务下
 * 由 {@link MessageHandleFunc} 注解的消息处理函数。
 *
 * @author flyan
 * @version 1.0
 * @date 10/19/22
 */
@Configuration
@Slf4j
public class AutoRegMessageHandlerListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private MessageIpcContext context;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();

        /* 初始化消息通信上下文环境 */
        String[] messageHandleServiceBeanNames = applicationContext.getBeanNamesForAnnotation(MessageHandleService.class);
        if(ArrayUtil.isEmpty(messageHandleServiceBeanNames)) {
            return;
        }

        for (String beanName : messageHandleServiceBeanNames) {
            if (applicationContext.getBean(beanName) instanceof IMessageHandleService messageHandleService) {
//                var mhsAnno = AnnotationUtils.findAnnotation(messageHandleService.getClass(), MessageHandleService.class);
                var clazz = MessageCoreUtil.getOriginClass(messageHandleService);
                var mhsAnno = clazz.getAnnotation(MessageHandleService.class);
                if(mhsAnno == null) {
                    log.error("[{}] Message handle service annotation is null!", messageHandleService.getClass().getSimpleName());
                }
                var serviceName = mhsAnno.service();
                log.info("[{}] message handle service has been registered", serviceName);
                /* 注册消息处理服务以及为该服务注册消息处理函数表 */
                MessageHandleFuncTable messageHandleFuncTable = new MessageHandleFuncTable();

                List<Method> messageHandleFunctions = ReflectUtil.getPublicMethods(clazz,
                        m -> m.isAnnotationPresent(MessageHandleFunc.class));
                for (Method func : messageHandleFunctions) {
                    MessageHandleFunc annotation = func.getAnnotation(MessageHandleFunc.class);
                    messageHandleFuncTable.registerHandleFunc(serviceName, annotation.messtype(), func);
                    /* 校验白名单配置 */
                    if (!annotation.auth()) {
                        context.addAuthWhiteListEntry(serviceName, annotation.messtype());
                    }
                }
                context.getMessageHandleServiceTable()
                        .registerService(messageHandleService, messageHandleFuncTable);
            }
        }
        log.info("Message handle services registration is completed\n"
                + context.toString());
    }

}
