package com.flyan.swiftmcweb.demo.biz.demo.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 说嗨响应 DTO
 *
 * @author flyan
 * @version 1.0
 * @date 10/20/22
 */
@Data
@Accessors(chain = true)
public class HiRespDTO implements Serializable {

    /**
     * 嗨！
     *
     * @mock hello flyan!
     */
    private String hi;

}
