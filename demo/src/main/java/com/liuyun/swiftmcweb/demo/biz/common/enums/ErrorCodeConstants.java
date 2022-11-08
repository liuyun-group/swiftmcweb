package com.liuyun.swiftmcweb.demo.biz.common.enums;


import com.liuyun.swiftmcweb.core.exception.ErrorCode;
import com.liuyun.swiftmcweb.core.exception.enums.ServiceErrorCodeRange;

/**
 * @author flyan
 * @version 1.0
 * @date 10/25/22
 */
public interface ErrorCodeConstants extends ServiceErrorCodeRange {

    // ========== AUTH 模块 1002000000 ==========
    ErrorCode AUTH_LOGIN_BAD_CREDENTIALS = new ErrorCode(1002000000, "登录失败，账号密码不正确");
    ErrorCode AUTH_LOGIN_USER_DISABLED = new ErrorCode(1002000001, "登录失败，账号被禁用");
    ErrorCode AUTH_LOGIN_CAPTCHA_NOT_FOUND = new ErrorCode(1002000003, "验证码不存在");
    ErrorCode AUTH_LOGIN_CAPTCHA_CODE_ERROR = new ErrorCode(1002000004, "验证码不正确");
    ErrorCode AUTH_THIRD_LOGIN_NOT_BIND = new ErrorCode(1002000005, "未绑定账号，需要进行绑定");
    ErrorCode AUTH_TOKEN_EXPIRED = new ErrorCode(1002000006, "Token 已经过期");
    ErrorCode AUTH_MOBILE_NOT_EXISTS = new ErrorCode(1002000007, "手机号不存在");

    // ========== USER 模块 1003000000 ==========
    ErrorCode DUPLICATE_USERNAME = new ErrorCode(1003000000, "重复的用户名");

    // ========== FILE 模块 1004000000 ==========
    ErrorCode ERR_FILE_NONE = new ErrorCode(1004000000, "上传文件为空");
    ErrorCode ERR_DIR_CREAT_FAILED = new ErrorCode(1004000001, "创建文件目录失败：{}");
    ErrorCode ERR_SAVE_UPLOAD_FILE_FAILED = new ErrorCode(1004000002, "存储上传文件失败，原因：{}");

}
