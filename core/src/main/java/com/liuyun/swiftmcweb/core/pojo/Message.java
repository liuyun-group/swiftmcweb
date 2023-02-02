package com.liuyun.swiftmcweb.core.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 消息通信-消息
 *
 * @apiNote 消息通信传输的消息
 * @author flyan
 * @version 1.0
 * @date 10/18/22
 */
//@Schema(description = "消息通信-消息")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Message<T> implements Serializable {

    /**
     * 服务
     *
     * @mock    SERVICE
     */
    @Schema(description = "服务", required = true, example = "DEMO")
    protected String service;

    /**
     * 消息类型
     *
     * @mock    coffee
     */
    @Schema(description = "消息类型", required = true, example = "hello_world")
    protected String messtype;

    /**
     * 消息体
     */
    @Schema(description = "消息体", required = true)
    protected T data;

    /**
     * 克隆一个基本消息，不带消息体
     *
     * @return
     */
    public <R> Message<R> baseClone() {
        return new Message<R>().setService(service).setMesstype(messtype);
    }

}
