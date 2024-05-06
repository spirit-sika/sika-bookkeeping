package cc.sika.bookkeeping.controller;

import cc.sika.bookkeeping.pojo.vo.Result;
import cn.dev33.satoken.annotation.SaCheckLogin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/greeting")
    @SaCheckLogin
    public Result<String> test() {
        return Result.success("Hello World");
    }
}
