package com.liuyun.swiftmcweb.demo.biz.sys.manager.auth.impl;

import com.liuyun.swiftmcweb.core.annotation.MessageHandleManager;
import com.liuyun.swiftmcweb.demo.biz.dal.CacheDao;
import com.liuyun.swiftmcweb.demo.biz.dal.UserDao;
import com.liuyun.swiftmcweb.demo.biz.sys.api.user.dto.UserInfoDTO;
import com.liuyun.swiftmcweb.demo.biz.sys.dal.user.UserDO;
import com.liuyun.swiftmcweb.demo.biz.sys.manager.auth.AuthManager;
import com.liuyun.swiftmcweb.core.util.exception.ServiceExceptionUtil;
import com.liuyun.swiftmcweb.demo.biz.common.enums.ErrorCodeConstants;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;

import static com.liuyun.swiftmcweb.core.util.exception.ServiceExceptionUtil.exception;

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

//    @Override
//    public Boolean validateAuth(String authorization) {
//        if(StrUtil.isBlank(authorization) || authorization.length() <= TOKEN_HEAD.length()) {
//            return false;
//        }
//
//        var token = authorization.substring(TOKEN_HEAD.length());
//        return !cacheDao.isExpire(token);
//    }
//
//    @Override
//    public Long getUserId(String authorization) {
//        if(StrUtil.isBlank(authorization) || authorization.length() <= TOKEN_HEAD.length()) {
//            return null;
//        }
//
//        var token = authorization.substring(TOKEN_HEAD.length());
//        return cacheDao.isExpire(token) ? null : (Long) cacheDao.get(token);
//    }
//
//    @Override
//    public String userId2Token(Long userId) {
//        if (userDao.exists(userId)) {
//            return JwtUtils.genToken(MapUtil.<String, Object>builder()
//                    .put(PAYLOAD_UID, userId)
//                    .build());
//        }
//        return null;
//    }

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
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.AUTH_LOGIN_BAD_CREDENTIALS);
        }
        UserInfoDTO ans = new UserInfoDTO();
        BeanUtils.copyProperties(userDO, ans);
        return ans;
    }

}
