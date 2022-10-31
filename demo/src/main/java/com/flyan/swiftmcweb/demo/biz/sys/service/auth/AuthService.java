package com.flyan.swiftmcweb.demo.biz.sys.service.auth;


import com.flyan.swiftmcweb.core.pojo.Message;
import com.flyan.swiftmcweb.core.pojo.ResponseMessage;
import com.flyan.swiftmcweb.core.web.service.BaseMessageHandleService;
import com.flyan.swiftmcweb.demo.biz.sys.api.auth.dto.CaptchaImageDTO;
import com.flyan.swiftmcweb.demo.biz.sys.api.auth.vo.LoginReqVO;
import com.flyan.swiftmcweb.demo.biz.sys.api.auth.vo.LoginResultDTO;
import com.flyan.swiftmcweb.demo.biz.sys.api.user.dto.UserInfoDTO;

/**
 * @author flyan
 * @version 1.0
 * @date 10/25/22
 */
public interface AuthService extends BaseMessageHandleService {

    /**
     * 获取验证码 Base64 图片
     *
     * @return
     */
    ResponseMessage<CaptchaImageDTO> getCaptchaImage(Message<?> message);

    /**
     * 获取授权的用户 id
     *
     * @param message 消息
     * @return
     */
    ResponseMessage<Long> getUserId(Message<?> message);

    /**
     * 获取授权的用户信息
     *
     * @param message 消息
     * @return
     */
    ResponseMessage<UserInfoDTO> getUserInfo(Message<?> message);

    /**
     * 账号+密码登录
     *
     * @param message
     * @return
     */
    ResponseMessage<LoginResultDTO> login(Message<LoginReqVO> message);

    /**
     * 登出注销
     *
     * @param message
     * @return
     */
    ResponseMessage<Boolean> logout(Message<?> message);


}
