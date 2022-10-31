/**
 * AUTH 验证服务相关类型定义
 */


/**
 * 验证码图片DTO
 */
export interface CaptchaImageDTO {

    /**
     * base64 验证码图片
     */
    base64Image: string;

    /**
     * 会话
     */
    session: string;

}

/**
 * 登录 ReqVO
 */
export interface LoginReqVO {

    /**
     * 密码
     */
    password: string;

    /**
     * 用户名
     */
    username: string;

    /**
     * 验证码
     */
    code: string;

    /**
     * 会话，验证码的唯一标识
     */
    session: string;

}

/**
 * 登录结果 DTO
 */
export interface LoginResultDTO {

    /**
     * 访问令牌
     */
    accessToken: string;

    /**
     * 用户 id
     */
    userId: number;

}



