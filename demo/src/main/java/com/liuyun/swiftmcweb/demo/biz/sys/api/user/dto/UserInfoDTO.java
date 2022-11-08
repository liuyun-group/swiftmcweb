package com.liuyun.swiftmcweb.demo.biz.sys.api.user.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户信息 DTO
 *
 * @author flyan
 * @version 1.0
 * @date 10/25/22
 */
@Data
@Accessors(chain = true)
public class UserInfoDTO implements Serializable {

    /**
     * 用户 id
     *
     * @mock    1
     */
    private Long id;

    /**
     * 用户名
     *
     * @mock    flyan
     */
    private String username;

    /**
     * 用户昵称
     *
     * @mock    辰晓夜
     */
    private String nickname;

    /**
     * 头像
     *
     * @mock    static/file/blackcat.png
     */
    private String headPic;

}
