package com.flyan.swiftmcweb.core.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 服务功能信息
 *
 * @author flyan
 * @version 1.0
 * @date 10/26/22
 */
@Data
@Accessors(chain = true)
public class ServiceFuncInfo {

    /**
     * 服务
     */
    protected String service;

    /**
     * 消息类型
     */
    protected String messtype;

}
