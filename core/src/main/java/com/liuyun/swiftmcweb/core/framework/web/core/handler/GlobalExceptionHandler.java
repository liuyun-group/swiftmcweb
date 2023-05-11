package com.liuyun.swiftmcweb.core.framework.web.core.handler;

import com.liuyun.swiftmcweb.core.exception.ServiceException;
import com.liuyun.swiftmcweb.core.exception.enums.GlobalErrorCodeConstants;
import com.liuyun.swiftmcweb.core.framework.web.core.util.WebFrameworkUtil;
import com.liuyun.swiftmcweb.core.pojo.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import static com.liuyun.swiftmcweb.core.exception.enums.GlobalErrorCodeConstants.FORBIDDEN;

/**
 * 全局异常处理器，将 Exception 翻译成 ResponseMessage + 对应的异常编号
 *
 * @author flyan
 * @version 1.0
 * @date 10/20/22
 */
@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理所有异常，主要是提供给 Filter 使用
     * 因为 Filter 不走 SpringMVC 的流程，但是我们又需要兜底处理异常，所以这里提供一个全量的异常处理过程，保持逻辑统一。
     *
     * @param request 请求
     * @param ex 异常
     * @return 通用返回
     */
    public ResponseMessage<Object> allExceptionHandler(HttpServletRequest request, Throwable ex) {
        if (ex instanceof MissingServletRequestParameterException e) {
            return missingServletRequestParameterExceptionHandler(e);
        }
        if (ex instanceof MethodArgumentTypeMismatchException e) {
            return methodArgumentTypeMismatchExceptionHandler(e);
        }
        if (ex instanceof MethodArgumentNotValidException e) {
            return methodArgumentNotValidExceptionExceptionHandler(e);
        }
        if (ex instanceof BindException e) {
            return bindExceptionHandler(e);
        }
        if (ex instanceof ConstraintViolationException e) {
            return constraintViolationExceptionHandler(e);
        }
        if (ex instanceof ValidationException e) {
            return validationException(e);
        }
        if (ex instanceof NoHandlerFoundException e) {
            return noHandlerFoundExceptionHandler(e);
        }
        if (ex instanceof HttpRequestMethodNotSupportedException e) {
            return httpRequestMethodNotSupportedExceptionHandler(e);
        }
        if (ex instanceof ServiceException e) {
            return serviceExceptionHandler(e);
        }
        if(ex instanceof AccessDeniedException e) {
            return accessDeniedExceptionHandler(request, e);
        }
        return defaultExceptionHandler(request, ex);
    }

    /**
     * 处理 SpringMVC 请求参数缺失
     *
     * 例如说，接口上设置了 @RequestParam("xx") 参数，结果并未传递 xx 参数
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseMessage<Object> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException ex) {
        log.warn("[missingServletRequestParameterExceptionHandler]", ex);
        return ResponseMessage.error("SYSTEM", null, GlobalErrorCodeConstants.BAD_REQUEST.getErrcode(), String.format("请求参数缺失:%s", ex.getParameterName()));
    }

    /**
     * 处理 SpringMVC 请求参数类型错误
     *
     * 例如说，接口上设置了 @RequestParam("xx") 参数为 Integer，结果传递 xx 参数类型为 String
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseMessage<Object> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException ex) {
        log.warn("[missingServletRequestParameterExceptionHandler]", ex);
        return ResponseMessage.error("SYSTEM", null, GlobalErrorCodeConstants.BAD_REQUEST.getErrcode(), String.format("请求参数类型错误:%s", ex.getMessage()));
    }

    /**
     * 处理 SpringMVC 参数校验不正确
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseMessage<Object> methodArgumentNotValidExceptionExceptionHandler(MethodArgumentNotValidException ex) {
        log.warn("[methodArgumentNotValidExceptionExceptionHandler]", ex);
        FieldError fieldError = ex.getBindingResult().getFieldError();
        assert fieldError != null; // 断言，避免告警
        return ResponseMessage.error("SYSTEM", null, GlobalErrorCodeConstants.BAD_REQUEST.getErrcode(), String.format("请求参数不正确:%s", fieldError.getDefaultMessage()));
    }

    /**
     * 处理 SpringMVC 参数绑定不正确，本质上也是通过 Validator 校验
     */
    @ExceptionHandler(BindException.class)
    public ResponseMessage<Object> bindExceptionHandler(BindException ex) {
        log.warn("[handleBindException]", ex);
        FieldError fieldError = ex.getFieldError();
        assert fieldError != null; // 断言，避免告警
        return ResponseMessage.error("SYSTEM", null, GlobalErrorCodeConstants.BAD_REQUEST.getErrcode(), String.format("请求参数不正确:%s", fieldError.getDefaultMessage()));
    }

    /**
     * 处理 Validator 校验不通过产生的异常
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseMessage<Object> constraintViolationExceptionHandler(ConstraintViolationException ex) {
        log.warn("[constraintViolationExceptionHandler]", ex);
        ConstraintViolation<?> constraintViolation = ex.getConstraintViolations().iterator().next();
        return ResponseMessage.error("SYSTEM", null, GlobalErrorCodeConstants.BAD_REQUEST.getErrcode(), String.format("请求参数不正确:%s", constraintViolation.getMessage()));
    }

    /**
     * 处理 Dubbo Consumer 本地参数校验时，抛出的 ValidationException 异常
     */
    @ExceptionHandler(value = ValidationException.class)
    public ResponseMessage<Object> validationException(ValidationException ex) {
        log.warn("[constraintViolationExceptionHandler]", ex);
        // 无法拼接明细的错误信息，因为 Dubbo Consumer 抛出 ValidationException 异常时，是直接的字符串信息，且人类不可读
        return ResponseMessage.error("SYSTEM", null, GlobalErrorCodeConstants.BAD_REQUEST);
    }

    /**
     * 处理 SpringMVC 请求地址不存在
     *
     * 注意，它需要设置如下两个配置项：
     * 1. spring.mvc.throw-exception-if-no-handler-found 为 true
     * 2. spring.mvc.static-path-pattern 为 /statics/**
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseMessage<Object> noHandlerFoundExceptionHandler(NoHandlerFoundException ex) {
        log.warn("[noHandlerFoundExceptionHandler]", ex);
        return ResponseMessage.error("SYSTEM", null, GlobalErrorCodeConstants.NOT_FOUND.getErrcode(), String.format("请求地址不存在:%s", ex.getRequestURL()));
    }

    /**
     * 处理 SpringMVC 请求方法不正确
     *
     * 例如说，A 接口的方法为 GET 方式，结果请求方法为 POST 方式，导致不匹配
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseMessage<Object> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException ex) {
        log.warn("[httpRequestMethodNotSupportedExceptionHandler]", ex);
        return ResponseMessage.error("SYSTEM", null, GlobalErrorCodeConstants.METHOD_NOT_ALLOWED.getErrcode(), String.format("请求方法不正确:%s", ex.getMessage()));
    }

    /**
     * 处理 Spring Security 权限不足的异常
     *
     * 来源是，使用 @PreAuthorize 注解，AOP 进行权限拦截
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseMessage<Object> accessDeniedExceptionHandler(HttpServletRequest req, AccessDeniedException ex) {
        log.warn("[accessDeniedExceptionHandler][userId({}) 无法访问服务方法[{}::{}]]", WebFrameworkUtil.getLoginUserId(req),
                req.getAttribute("service"),
                req.getAttribute("messtype"),
                ex);
        return ResponseMessage.error("SYSTEM", null, FORBIDDEN.getErrcode(), FORBIDDEN.getMsg());
    }

    /**
     * 处理业务异常 ServiceException
     *
     * 例如说，商品库存不足，用户手机号已存在。
     */
    @ExceptionHandler(value = ServiceException.class)
    public ResponseMessage<Object> serviceExceptionHandler(ServiceException ex) {
        log.info("[serviceExceptionHandler] {} {}", ex.getErrcode(), ex.getMsg());
        return ResponseMessage.error("SYSTEM", null, ex.getErrcode(), ex.getMsg());
    }

    /**
     * 处理系统异常，兜底处理所有的一切
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseMessage<Object> defaultExceptionHandler(HttpServletRequest req, Throwable ex) {
        log.error("[defaultExceptionHandler] {}", ex.getMessage());
        // 返回 ERROR ResponseMessage
        return ResponseMessage.error("SYSTEM", null,
                GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getErrcode(), String.format("系统异常:%s", ex.getMessage()));
    }

}
