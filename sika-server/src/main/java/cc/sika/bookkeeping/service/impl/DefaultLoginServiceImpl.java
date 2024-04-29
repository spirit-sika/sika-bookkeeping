package cc.sika.bookkeeping.service.impl;

import cc.sika.bookkeeping.constant.AuthenticationConstant;
import cc.sika.bookkeeping.exception.LoginException;
import cc.sika.bookkeeping.mapper.SikaUserMapper;
import cc.sika.bookkeeping.pojo.dto.LoginDTO;
import cc.sika.bookkeeping.pojo.po.SikaUser;
import cc.sika.bookkeeping.pojo.vo.LoginVO;
import cc.sika.bookkeeping.service.CaptchaService;
import cc.sika.bookkeeping.service.LoginService;
import cc.sika.bookkeeping.service.SikaUserService;
import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service("loginService")
@RequiredArgsConstructor
public class DefaultLoginServiceImpl implements LoginService {

    private final CaptchaService captchaService;
    private final SikaUserService userService;
    private final SikaUserMapper sikaUserMapper;

    @Override
    public String login(LoginDTO loginDTO) {
        doLogin(loginDTO);
        return StpUtil.getTokenValue();
    }

    @Override
    public LoginVO loginAndGetVO(LoginDTO loginDTO) {
        // 校验并登录
        doLogin(loginDTO);

        // 生成用户vo
        SikaUser user = sikaUserMapper.selectOne(
                new LambdaQueryWrapper<>(SikaUser.class)
                        .eq(StringUtils.hasText(loginDTO.getPhone()),
                                SikaUser::getPhone,
                                loginDTO.getPhone())
        );
        LoginVO loginVO = BeanUtil.copyProperties(user, LoginVO.class);
        loginVO.setToken(StpUtil.getTokenValue());
        return loginVO;
    }

    /**
     * 执行登录逻辑, 校验用户表单信息, 成功后将用户信息放入 Satoken 的会话容器缓存
     * @param loginDTO 用户表单信息
     */
    private void doLogin(LoginDTO loginDTO) {

        // 校验验证码
        captchaService.checkCaptcha(loginDTO.getCodeKey(), loginDTO.getCode());


        // 验证用户信息
        String phone = loginDTO.getPhone();
        String password = SaSecureUtil.sha256(loginDTO.getPassword());
        SikaUser loginUser = userService.getOne(new LambdaQueryWrapper<SikaUser>()
                .eq(StringUtils.hasText(phone), SikaUser::getPhone, phone)
                .eq(StringUtils.hasText(password), SikaUser::getPassword, password), false);

        // 校验不通过
        if (Objects.isNull(loginUser)) {
            SikaUser phoneExists = userService.getOne(new LambdaQueryWrapper<>(SikaUser.class)
                    .eq(StringUtils.hasText(phone), SikaUser::getPhone, phone));
            if (!Objects.isNull(phoneExists)) {
                throw new LoginException("该手机号码未注册!");
            }
            throw new LoginException("手机或密码错误!");
        }

        Long userId = loginUser.getUserId();
        // 记住我
        if (loginDTO.getRememberMe()) {
            StpUtil.login(userId,
                    new SaLoginModel()
                            .setTimeout(60 * 60 * 24 * 15)
                            .setDevice(loginDTO.getDeviceType().getType()));
        } else {
            StpUtil.login(userId, loginDTO.getDeviceType().getType());
        }

        // 缓存用户信息
        StpUtil.getSession().set(AuthenticationConstant.CURRENT_USER, sikaUserMapper.selectById(userId));
    }


}
