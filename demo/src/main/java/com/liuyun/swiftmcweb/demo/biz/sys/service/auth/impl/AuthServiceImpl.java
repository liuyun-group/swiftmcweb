package com.liuyun.swiftmcweb.demo.biz.sys.service.auth.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.liuyun.swiftmcweb.core.annotation.MessageHandleFunc;
import com.liuyun.swiftmcweb.core.annotation.MessageHandleService;
import com.liuyun.swiftmcweb.core.framework.security.core.enums.JwtConsts;
import com.liuyun.swiftmcweb.core.pojo.Message;
import com.liuyun.swiftmcweb.core.pojo.ResponseMessage;
import com.liuyun.swiftmcweb.core.util.exception.ServiceExceptionUtil;
import com.liuyun.swiftmcweb.core.util.jwt.JwtUtils;
import com.liuyun.swiftmcweb.core.web.service.impl.MessageHandleServiceImpl;
import com.liuyun.swiftmcweb.demo.biz.common.enums.ErrorCodeConstants;
import com.liuyun.swiftmcweb.demo.biz.dal.CacheDao;
import com.liuyun.swiftmcweb.demo.biz.sys.api.auth.dto.CaptchaImageDTO;
import com.liuyun.swiftmcweb.demo.biz.sys.api.auth.vo.LoginReqVO;
import com.liuyun.swiftmcweb.demo.biz.sys.api.auth.vo.LoginResultDTO;
import com.liuyun.swiftmcweb.demo.biz.sys.api.user.dto.UserInfoDTO;
import com.liuyun.swiftmcweb.demo.biz.sys.manager.auth.AuthManager;
import com.liuyun.swiftmcweb.demo.biz.sys.manager.user.UserManager;
import com.liuyun.swiftmcweb.demo.biz.sys.service.auth.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.liuyun.swiftmcweb.core.framework.security.core.enums.JwtConsts.TOKEN_HEAD;
import static com.liuyun.swiftmcweb.core.pojo.ResponseMessage.success;


/**
 * 服务 - 验证服务
 *
 * @author flyan
 * @version 1.0
 * @date 10/25/22
 */
@MessageHandleService(service = "AUTH")
@RestController /* 由于 smart-doc 是通过源码生成 api 文档的，它只认识 .java 文件是否包含
                 * Controller、RestController 等注解，所以这里为了让 smart-doc 生成文档，
                 * 只能这样加上注解了，实际上不加也不会影响系统功能，暂未想到更好的解决方案。
                 */
@Slf4j
public class AuthServiceImpl 
        extends MessageHandleServiceImpl<AuthManager>
        implements AuthService {

    private static final int WIDTH = 160;
    private static final int HEIGHT = 60;
    private static final Long TIMEOUT_SECONDS = 3 * 60L;

    @Resource
    private CacheDao cacheDao;

    @Resource
    private UserManager userManager;

    /**
     * 获取验证码 Base64 图片
     *
     * @apiNote <p>
     *     {
     *         "service": "AUTH",
     *         "messtype": "captcha_image"
     *     }
     * </p>
     * @param message
     * @return
     */
    @MessageHandleFunc(messtype = "captcha_image", auth = false)
    @PostMapping("AUTH/captcha_image")
    @Override
    public ResponseMessage<CaptchaImageDTO> getCaptchaImage(@RequestBody @Valid Message<?> message) {
        var captcha = CaptchaUtil.createCircleCaptcha(WIDTH, HEIGHT);
        var session = IdUtil.fastSimpleUUID();
        cacheDao.set(session, captcha.getCode(), TIMEOUT_SECONDS);
        log.info("[{}]captcha code: {}", session, captcha.getCode());

        return success(message, new CaptchaImageDTO()
                .setSession(session)
                .setBase64Image(captcha.getImageBase64Data())
        );
    }

    /**
     * 获取授权的用户 id
     *
     * @apiNote <p>
     *     {
     *         "service": "AUTH",
     *         "messtype": "user_id"
     *     }
     * </p>
     * @param message
     * @return  用户id
     */
    @MessageHandleFunc(messtype = "user_id")
    @PostMapping("AUTH/user_id")
    @Override
    public ResponseMessage<Long> getUserId(@RequestBody @Valid Message<?> message) {
        HttpServletRequest request = getRequest();
        var authorization = request.getHeader(JwtConsts.AUTH_HEADER);
        Long userId = baseManager.getUserId(authorization);
        return (ResponseMessage<Long>) new ResponseMessage<Long>(message).setData(userId);
    }

    /**
     * 获取授权的用户信息
     *
     * @apiNote <p>
     *     {
     *         "service": "AUTH",
     *         "messtype": "user_info"
     *     }
     * </p>
     * @param message
     * @return
     */
    @MessageHandleFunc(messtype = "user_info")
    @PostMapping("AUTH/user_info")
    @Override
    public ResponseMessage<UserInfoDTO> getUserInfo(@RequestBody @Valid Message<?> message) {
        HttpServletRequest request = getRequest();
        var authorization = request.getHeader(JwtConsts.AUTH_HEADER);
        var ans = new ResponseMessage<UserInfoDTO>(message);

        if(!(StrUtil.isBlank(authorization) || authorization.length() <= TOKEN_HEAD.length())) {
            var token = authorization.substring(TOKEN_HEAD.length());
            if(!cacheDao.isExpire(token)) {
                var uid = (Long) cacheDao.get(token);
                ans.setData(userManager.getUserInfo(uid));
            }
        }

        return ans;
    }

    /**
     * 账号+密码登录
     *
     * @apiNote <p>
     *     {
     *         "service": "AUTH",
     *         "messtype": "login",
     *         "data": {
     *             "username": "admin",
     *             "password": "1234567890",
     *             "code": "1024",
     *             "session": ""
     *         }
     *     }
     * </p>
     * @param message
     * @return
     */
    @MessageHandleFunc(messtype = "login", auth = false)
    @PostMapping("AUTH/login")
    @Override
    public ResponseMessage<LoginResultDTO> login(@RequestBody @Valid Message<LoginReqVO> message) {
        var reqVo = message.getData();
        /* 校验验证码 */
        verifyCaptcha(reqVo);
        /* 账号密码验证 */
        var user = baseManager.authenticate(reqVo.getUsername(), reqVo.getPassword());
        var token = baseManager.userId2Token(user.getId());
        cacheDao.set(token, user.getId(), JwtUtils.EXPIRE_DATE);
        return success(message, new LoginResultDTO(user.getId(), token));
    }

    /**
     * 登出注销
     *
     * @apiNote <p>
     *     {
     *         "service": "AUTH",
     *         "messtype": "login"
     *     }
     * </p>
     * @param message
     * @return  注销成功与否
     */
    @MessageHandleFunc(messtype = "logout")
    @PostMapping("AUTH/logout")
    @Override
    public ResponseMessage<Boolean> logout(@RequestBody @Valid Message<?> message) {
        HttpServletRequest request = getRequest();
        var authorization = request.getHeader(JwtConsts.AUTH_HEADER);
        var ans = new ResponseMessage<Boolean>(message);

        if(!(StrUtil.isBlank(authorization) || authorization.length() <= TOKEN_HEAD.length())) {
            var token = authorization.substring(TOKEN_HEAD.length());
            cacheDao.delete(token);
            ans.setData(true);
        }

        return ans;
    }

    private void verifyCaptcha(LoginReqVO loginReqVO) {
        var code = baseManager.getCaptchaCode(loginReqVO.getSession());
        if(code == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.AUTH_LOGIN_CAPTCHA_NOT_FOUND);
        }
        if(!code.equalsIgnoreCase(loginReqVO.getCode())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.AUTH_LOGIN_CAPTCHA_CODE_ERROR);
        }
        baseManager.deleteCaptchaCodeSession(loginReqVO.getSession());
    }

}
