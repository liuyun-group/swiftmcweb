package com.flyan.swiftmcweb.demo.biz.sys.api.auth.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 验证码图片DTO
 *
 * @author flyan
 * @version 1.0
 * @date 10/25/22
 */
@Data
@Accessors(chain = true)
public class CaptchaImageDTO {

    /**
     * 会话
     */
    private String session;

    /**
     * 验证码base64图片
     */
    private String base64Image;

}
