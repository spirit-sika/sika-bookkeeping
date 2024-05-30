package cc.sika.bookkeeping.controller;

import cc.sika.bookkeeping.pojo.vo.Result;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @GetMapping("/greeting")
    public Result<String> test() {
        log.info("greeting接口被请求");
        return Result.successMessage("Hello World");
    }

    @GetMapping("/greeting-auth")
    @SaCheckLogin
    public Result<String> testAuth() {
        log.info("greeting-auth接口被请求");
        return Result.successMessage("请求已携带登录信息");
    }

    @GetMapping("/greeting-auth-admin")
    @SaCheckRole({"admin"})
    public Result<String> testAuthRole() {
        return Result.successMessage("请求已携带管理员身份信息");
    }
}
