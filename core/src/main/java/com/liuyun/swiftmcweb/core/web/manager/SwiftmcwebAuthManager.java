package com.liuyun.swiftmcweb.core.web.manager;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.liuyun.swiftmcweb.core.annotation.MessageHandleFunc;
import com.liuyun.swiftmcweb.core.annotation.MessageHandleManager;
import com.liuyun.swiftmcweb.core.util.jwt.JwtUtils;

import static com.liuyun.swiftmcweb.core.framework.security.core.enums.JwtConsts.PAYLOAD_UID;
import static com.liuyun.swiftmcweb.core.framework.security.core.enums.JwtConsts.TOKEN_HEAD;

/**
 * swiftmcweb 框架用户验证管理器，必须实现且使用 {@link MessageHandleManager} 注释。
 * 如果未实现该接口，则无法正常使用 swiftmcweb，swiftmcweb 将无法为其提供 {@link MessageHandleFunc}
 * 服务功能注解提供的用户登录校验功能。
 *
 * @author flyan
 * @version 1.0
 * @date 10/27/22
 */
public interface SwiftmcwebAuthManager extends IMessageHandleManager {

    /**
     * 校验授权
     *
     * @param authorization 授权
     * @return
     */
    default Boolean validateAuth(String authorization) {
        if(StrUtil.isBlank(authorization) || authorization.length() <= TOKEN_HEAD.length()) {
            return false;
        }

        var token = authorization.substring(TOKEN_HEAD.length());
        return JwtUtils.verifyToken(token) == JwtUtils.TokenVerifyResult.SUCCESS;
    }

    /**
     * 获取授权的用户id
     *
     * @param authorization 授权
     * @return
     */
    default Long getUserId(String authorization) {
        if(StrUtil.isBlank(authorization) || authorization.length() <= TOKEN_HEAD.length()) {
            return null;
        }

        var token = authorization.substring(TOKEN_HEAD.length());
        var verResult = JwtUtils.verifyToken(token);
        if(verResult != JwtUtils.TokenVerifyResult.SUCCESS) {
            return null;
        }

        var jwtPayloads = verResult.getJwt().getPayloads();
        return jwtPayloads.getLong(PAYLOAD_UID);
    }

    /**
     * 颁发授权
     *
     * @param userId    用户id
     * @return
     */
    default String userId2Token(Long userId) {
        return JwtUtils.genToken(MapUtil.<String, Object>builder()
                .put(PAYLOAD_UID, userId)
                .build());
    }

}
