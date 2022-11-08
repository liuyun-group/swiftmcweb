package com.liuyun.swiftmcweb.demo.biz.demo.api.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 说嗨 ReqVO
 *
 * @author flyan
 * @version 1.0
 * @date 10/20/22
 */
@Data
@Accessors(chain = true)
public class HiReqVO implements Serializable {

    /**
     * 名字
     *
     * @mock    flyan
     */
    private String name;

}
