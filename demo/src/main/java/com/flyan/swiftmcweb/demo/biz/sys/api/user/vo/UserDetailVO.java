package com.flyan.swiftmcweb.demo.biz.sys.api.user.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户详情 VO
 *
 * @author flyan
 * @version 1.0
 * @date 10/26/22
 */
@Data
@Accessors(chain = true)
public class UserDetailVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickname;

}
