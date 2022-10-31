package com.flyan.swiftmcweb.core.util.jwt;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.flyan.swiftmcweb.core.util.time.TimeUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 用户登入获取token返给前端
 *
 * @author flyan
 **/
public class JwtUtils {

    /**
     * 过期时间
     */
    public static long EXPIRE_DATE = TimeUtils.DAY_MILLIS;

    public static void init(long expireDate) {
        JwtUtils.EXPIRE_DATE = expireDate;
    }

    /**
     * token秘钥
     */
    private static final String TOKEN_SECRET = "chinauni.liuyun.oa.server";

    /**
     * 生成 token。
     *
     * @param payload
     * @return
     */
    public static String genToken(Map<String, Object> payload){
        DateTime now = DateUtil.date();
        DateTime exp = DateUtil.date(now.getTime() + EXPIRE_DATE);

        Map<String, Object> payloadWithAuthDate = MapUtil.<String, Object>builder()
                /* 颁发、过期、生效时间 */
                .put(JWTPayload.ISSUED_AT, now)
                .put(JWTPayload.EXPIRES_AT, exp)
                .put(JWTPayload.NOT_BEFORE, now)
                /* 载荷 */
                .putAll(payload)
                .build();

        return JWTUtil.createToken(payloadWithAuthDate, TOKEN_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    /**
     *  验证 token
     *
     * @param token  需要校验的串
     **/
    public static TokenVerifyResult verifyToken(String token){
        JWT jwt = JWTUtil.parseToken(token);

        if(!jwt.setKey(TOKEN_SECRET.getBytes(StandardCharsets.UTF_8)).verify()) {
            return TokenVerifyResult.BAD_KEY;
        }

        if(!jwt.validate(0)) {
            return TokenVerifyResult.TIME_VALIDATE_FAIL;
        }

        /* 成功后带上解析后的 jwt */
        return TokenVerifyResult.SUCCESS.setJwt(jwt);
    }

    /**
     * 解析 token 获得 jwt
     *
     * @param token
     * @return
     */
    public static JWT parseToken(String token) {
        return JWTUtil.parseToken(token);
    }

    public enum TokenVerifyResult {
        /* token 校验结果 */
        SUCCESS("成功"),
        BAD_KEY("错误的 key"),
        TIME_VALIDATE_FAIL("token已失效");

        @Getter
        private final String message;

        @Setter
        @Getter
        @Accessors(chain = true)
        private JWT jwt;

        TokenVerifyResult(String message) {
            this.message = message;
        }

    }

}