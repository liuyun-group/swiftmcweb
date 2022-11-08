package com.liuyun.swiftmcweb.demo.biz.sys.api.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 登录结果 DTO
 *
 * @author flyan
 * @version 1.0
 * @date 10/25/22
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class LoginResultDTO implements Serializable {

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 访问令牌
     */
    private String accessToken;

}
