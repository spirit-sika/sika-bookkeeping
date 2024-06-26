package cc.sika.bookkeeping.handler;

import cc.sika.bookkeeping.exception.CaptchaException;
import cc.sika.bookkeeping.exception.LoginException;
import cc.sika.bookkeeping.exception.RegisterException;
import cc.sika.bookkeeping.pojo.vo.Result;
import cn.dev33.satoken.exception.NotLoginException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * 登录异常处理
     * @param e 异常对象
     * @return 未鉴权识别码401与错误消息
     */
    @ExceptionHandler(LoginException.class)
    public Result<String> loginException(LoginException e) {
        return Result.error(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }

    /**
     * 验证码异常处理
     * @param e 验证码异常
     * @return 为携带验证码键或者验证码值错误信息, 错误码400
     */
    @ExceptionHandler(CaptchaException.class)
    public Result<String> captchaException(CaptchaException e) {
        return Result.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(RegisterException.class)
    public Result<String> registerException(RegisterException e) {
        return Result.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }


//    @ExceptionHandler(Exception.class)
//    public Result<String> exception(Exception e) {
//        return Result.error("接口请求错误!");
//    }

    @ExceptionHandler(NotLoginException.class)
    public Result<String> notLoginException(NotLoginException e) {
        return Result.error("账号未登录或登录信息已过期");
//        return Result.error(e.getMessage());
    }
}
