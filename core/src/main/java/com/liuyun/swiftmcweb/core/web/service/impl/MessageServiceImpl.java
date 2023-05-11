package com.liuyun.swiftmcweb.core.web.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.liuyun.swiftmcweb.core.annotation.MessageHandleService;
import com.liuyun.swiftmcweb.core.context.MessageIpcContext;
import com.liuyun.swiftmcweb.core.framework.web.core.handler.GlobalExceptionHandler;
import com.liuyun.swiftmcweb.core.pojo.Message;
import com.liuyun.swiftmcweb.core.pojo.ResponseMessage;
import com.liuyun.swiftmcweb.core.util.exception.ServiceExceptionUtil;
import com.liuyun.swiftmcweb.core.util.validate.ValidateUtil;
import com.liuyun.swiftmcweb.core.web.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static com.liuyun.swiftmcweb.core.exception.enums.GlobalErrorCodeConstants.*;

/**
 * 消息服务实现，这里只提供一个消息回合机制的实现，即发送一条消息给某服务，并接收其响应消息。
 *
 * @author flyan
 * @version 1.0
 * @date 10/18/22
 */
@Slf4j
@MessageHandleService(service = "MESSAGE")
public class MessageServiceImpl implements MessageService {

    public final String SERVICE_NAME = getServiceName();

    @Resource
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private MessageIpcContext context;

    @SuppressWarnings("unchecked")
    @Override
    public ResponseMessage<Object> sendrec(Message<Object> message) {
        final var FUNC_NAME = "sendrec";
        final var service = message.getService();
        final var messtype = message.getMesstype();
        final var data = message.getData();

        /* 获取服务信息 */
        var serviceTable = context.getMessageHandleServiceTable();
        if(!serviceTable.serviceExist(service)) {
            log.warn("Send a message to a non-existent service({})", message.getService());
            return error(SERVICE_NAME, FUNC_NAME, SERVICE_NOT_EXIST.getErrcode(),
                    ServiceExceptionUtil.doFormat(SERVICE_NOT_EXIST.getErrcode(), SERVICE_NOT_EXIST.getMsg(), service));
        }

        /* 获取消息处理函数 */
        var messageHandleServiceInfo = serviceTable.getService(service);
        var handleFuncTable = messageHandleServiceInfo.getHandleFuncTable();
        if(!handleFuncTable.handleFuncExist(messtype)) {
            return error(SERVICE_NAME, FUNC_NAME, REQUEST_FUNCTION_NOT_IN_SERVICE.getErrcode(),
                    ServiceExceptionUtil.doFormat(REQUEST_FUNCTION_NOT_IN_SERVICE.getErrcode(), REQUEST_FUNCTION_NOT_IN_SERVICE.getMsg(), messtype, service));
        }

        /* 传递消息给该处理函数，得到响应 */
        var func = handleFuncTable.getHandleFuncName(messtype);
        var genericParameterTypes = func.getGenericParameterTypes();
        if (data != null) {
            /* 需要类型转换！获取返回值类型中的泛型信息，所以要进一步判断，即判断获取的返回值类型是否是参数化类型 ParameterizedType */
            if (genericParameterTypes.length == 1) {
                ParameterizedType parameterizedType = (ParameterizedType) genericParameterTypes[0];
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if(actualTypeArguments != null && actualTypeArguments.length == 1) {
                    Class<?> realType = (Class<?>) actualTypeArguments[0];
                    /* 转换消息体为处理函数需要的对象并通过反射调用，这里有性能消耗 */
                    try {
                        var to = JSON.to(realType, data);
                        var validateResult = ValidateUtil.validate(to);
                        /* 消息体字段校验，失败则返回错误 */
                        if(!validateResult.success) {
                            return error(SERVICE_NAME, FUNC_NAME, MESSAGE_DATA_VALIDATE_FAILED.getErrcode(),
                                    ServiceExceptionUtil.doFormat(MESSAGE_DATA_VALIDATE_FAILED.getErrcode(), MESSAGE_DATA_VALIDATE_FAILED.getMsg(), validateResult.errorMsg));
                        }
                        message.setData(to);
                    } catch (JSONException ex) {
                        log.error("Message body trans to object failed, ex: {}", ex.toString());
                        return error(SERVICE_NAME, FUNC_NAME, MESSAGE_DATA_CANNOT_CAST.getErrcode(),
                                ServiceExceptionUtil.doFormat(MESSAGE_DATA_CANNOT_CAST.getErrcode(), MESSAGE_DATA_CANNOT_CAST.getMsg()));
                    }
                }
            }
        }

        /* 反射调用实际的消息服务方法 */
        try {
            if(func.invoke(messageHandleServiceInfo.getService(), message) instanceof ResponseMessage responseMessage) {
                return responseMessage;
            } else {
                /* 有些处理函数不需要响应 */
                return null;
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            /* 处理所有异常 */
            Throwable cause = e.getCause();
            cause.printStackTrace();
            return globalExceptionHandler.allExceptionHandler(getRequest(), cause);
        }

    }

}
