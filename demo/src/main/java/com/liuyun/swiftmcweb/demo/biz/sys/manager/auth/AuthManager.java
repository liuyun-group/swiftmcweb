package com.liuyun.swiftmcweb.demo.biz.sys.manager.auth;

import com.liuyun.swiftmcweb.core.web.manager.SwiftmcwebAuthManager;
import com.liuyun.swiftmcweb.demo.biz.sys.api.user.dto.UserInfoDTO;

/**
 * 实现 swiftmcweb 框架要求的 AuthManager，并添加拓展功能
 *
 * @author flyan
 * @version 1.0
 * @date 10/27/22
 */
public interface AuthManager extends SwiftmcwebAuthManager {

    /**
     * 获取会话的验证码
     *
     * @param session   验证码会话
     * @return
     */
    String getCaptchaCode(String session);

    /**
     * 删除验证码会话
     *
     * @param session   验证码会话
     */
    void deleteCaptchaCodeSession(String session);

    UserInfoDTO authenticate(String username, String password);

}
