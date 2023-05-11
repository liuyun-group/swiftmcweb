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

    /**
     * 判断是否有权限
     *
     * @param permission 权限
     * @return 是否
     */
    default boolean hasPermission(String permission) {
        return hasAnyPermissions(permission);
    }

    /**
     * 判断是否有权限，任一一个即可
     *
     * @param permissions 权限
     * @return 是否
     */
    default boolean hasAnyPermissions(String... permissions) {
        return true;
    }

    /**
     * 判断是否有角色
     *
     * 注意，角色使用的是 SysRoleDO 的 code 标识
     *
     * @param role 角色
     * @return 是否
     */
    default boolean hasRole(String role) {
        return hasAnyRoles(role);
    }

    /**
     * 判断是否有角色，任一一个即可
     *
     * @param roles 角色数组
     * @return 是否
     */
    default boolean hasAnyRoles(String... roles) {
        return true;
    }

}
