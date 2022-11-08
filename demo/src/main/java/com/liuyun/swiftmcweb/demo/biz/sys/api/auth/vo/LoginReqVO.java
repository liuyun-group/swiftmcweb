package com.liuyun.swiftmcweb.demo.biz.sys.api.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 登录 ReqVO
 *
 * @author flyan
 * @version 1.0
 * @date 10/25/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginReqVO {

    /**
     * 账号
     *
     * @mock admin
     */
    @NotEmpty(message = "登录账号不能为空")
    @Length(min = 4, max = 16, message = "账号长度为 4-16 位")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "账号格式为数字以及字母")
    private String username;

    /**
     * 密码
     *
     * @mock 1234567890
     */
    @NotEmpty(message = "密码不能为空")
    @Length(min = 4, max = 16, message = "密码长度为 4-16 位")
    private String password;

    /**
     * 验证码
     *
     * @mock 1024
     * @apiNote 验证码开启时，需要传递
     */
    @NotBlank(message = "验证码不能为空")
    private String code;

    /**
     * 验证码会话，验证码的唯一标识
     *
     * @apiNote 验证码开启时，需要传递
     */
    @NotBlank(message = "验证码会话不能为空")
    private String session;

}
