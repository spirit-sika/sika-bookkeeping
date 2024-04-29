package cc.sika.bookkeeping.controller;

import cc.sika.bookkeeping.pojo.dto.LoginDTO;
import cc.sika.bookkeeping.service.CaptchaService;
import cc.sika.bookkeeping.service.LoginService;
import cc.sika.bookkeeping.pojo.vo.CaptchaVO;
import cc.sika.bookkeeping.pojo.vo.LoginVO;
import cc.sika.bookkeeping.pojo.vo.Result;
import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("user")
@AllArgsConstructor
public class LoginController {

    private final CaptchaService captchaService;
    private final LoginService loginService;


    @Operation(summary = "登录接口, 登录成功返回用户信息vo与token")
    @ApiResponse(content = @Content(schema = @Schema(implementation = LoginVO.class)))
    @PostMapping("login")
    @SaIgnore
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        return Result.success(loginService.loginAndGetVO(loginDTO));
    }

    @Operation(summary = "获取验证码接口")
    @ApiResponse(content = @Content(schema = @Schema(implementation = CaptchaVO.class)))
    @GetMapping("captcha")
    @SaIgnore
    public Result<CaptchaVO> getCaptcha() {
        CaptchaVO captchaVO = captchaService.generateCaptcha();
        return Result.success(captchaVO);
    }
}
