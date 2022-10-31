package com.flyan.swiftmcweb.demo.biz.sys.manager.auth.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.flyan.swiftmcweb.core.annotation.MessageHandleManager;
import com.flyan.swiftmcweb.core.util.jwt.JwtUtils;
import com.flyan.swiftmcweb.demo.biz.dal.CacheDao;
import com.flyan.swiftmcweb.demo.biz.dal.UserDao;
import com.flyan.swiftmcweb.demo.biz.sys.api.user.dto.UserInfoDTO;
import com.flyan.swiftmcweb.demo.biz.sys.dal.user.UserDO;
import com.flyan.swiftmcweb.demo.biz.sys.manager.auth.AuthManager;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;

import static com.flyan.swiftmcweb.core.framework.security.core.enums.JwtConsts.PAYLOAD_UID;
import static com.flyan.swiftmcweb.core.framework.security.core.enums.JwtConsts.TOKEN_HEAD;
import static com.flyan.swiftmcweb.core.util.exception.ServiceExceptionUtil.exception;
import static com.flyan.swiftmcweb.demo.biz.common.enums.ErrorCodeConstants.AUTH_LOGIN_BAD_CREDENTIALS;

/**
 * @author flyan
 * @version 1.0
 * @date 10/27/22
 */
@MessageHandleManager(name = "authManager")
public class AuthManagerImpl implements AuthManager {

    @Resource
    private CacheDao cacheDao;

    @Resource
    private UserDao userDao;

    @Override
    public Boolean validateAuth(String authorization) {
        if(StrUtil.isBlank(authorization) || authorization.length() <= TOKEN_HEAD.length()) {
            return false;
        }

        var token = authorization.substring(TOKEN_HEAD.length());
        return !cacheDao.isExpire(token);
    }

    @Override
    public Long getUserId(String authorization) {
        if(StrUtil.isBlank(authorization) || authorization.length() <= TOKEN_HEAD.length()) {
            return null;
        }

        var token = authorization.substring(TOKEN_HEAD.length());
        return cacheDao.isExpire(token) ? null : (Long) cacheDao.get(token);
    }

    @Override
    public String userId2Token(Long userId) {
        if (userDao.exists(userId)) {
            return JwtUtils.genToken(MapUtil.<String, Object>builder()
                    .put(PAYLOAD_UID, userId)
                    .build());
        }
        return null;
    }

    @Override
    public String getCaptchaCode(String session) {
        return (String) cacheDao.get(session);
    }

    @Override
    public void deleteCaptchaCodeSession(String session) {
        cacheDao.delete(session);
    }

    @Override
    public UserInfoDTO authenticate(String username, String password) {
        UserDO userDO = userDao.list().stream()
                .filter(x -> x.getUsername().equals(username)).findFirst().orElse(null);
        if(userDO == null || !userDO.getPassword().equals(password)) {
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        UserInfoDTO ans = new UserInfoDTO();
        BeanUtils.copyProperties(userDO, ans);
        return ans;
    }

}
