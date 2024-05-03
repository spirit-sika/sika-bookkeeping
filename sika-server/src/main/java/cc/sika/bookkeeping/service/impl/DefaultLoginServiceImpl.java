package cc.sika.bookkeeping.service.impl;

import cc.sika.bookkeeping.constant.AuthenticationConstant;
import cc.sika.bookkeeping.exception.LoginException;
import cc.sika.bookkeeping.exception.RegisterException;
import cc.sika.bookkeeping.mapper.SikaUserMapper;
import cc.sika.bookkeeping.mapper.SikaUserRoleMapper;
import cc.sika.bookkeeping.pojo.dto.LoginDTO;
import cc.sika.bookkeeping.pojo.dto.RegisterDTO;
import cc.sika.bookkeeping.pojo.po.SikaUser;
import cc.sika.bookkeeping.pojo.po.SikaUserRole;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service("loginService")
@RequiredArgsConstructor
public class DefaultLoginServiceImpl implements LoginService {

    private final CaptchaService captchaService;
    private final SikaUserService userService;
    private final SikaUserRoleMapper sikaUserRoleMapper;
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

    @Override
    @Transactional(rollbackFor = RegisterException.class)
    public String register(RegisterDTO registerDTO) {
        LambdaQueryWrapper<SikaUser> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        /* 检查是否存在相同手机号码 */
        SikaUser isUserExists = sikaUserMapper.selectOne(
                userLambdaQueryWrapper.eq(SikaUser::getPhone, registerDTO.getPhone()),
                false);
        if (!Objects.isNull(isUserExists)) {
            throw new RegisterException("手机号码已被注册!");
        }
        /* 构建用户实体 */
        SikaUser sikaUser = SikaUser.builder()
                .phone(registerDTO.getPhone())
                .username(registerDTO.getPhone())
                .nickname(registerDTO.getNickname()).build();
        // 性别处理
        Byte sex = registerDTO.getSex();
        if (!Objects.isNull(sex) && (1 == sex.intValue() || 2 == sex.intValue())) {
            sikaUser.setSex(sex);
        }
        else {
            sikaUser.setSex(Byte.valueOf("0"));
        }
        String email = registerDTO.getEmail();
        // 邮箱格式校验由接口参数校验完成, 接口参数校验可以为null
        if (StringUtils.hasText(email)) {
            sikaUser.setEmail(email);
        }
        // 密码, 未填写时默认为手机号码
        String userPassword = registerDTO.getPassword();
        if (StringUtils.hasText(userPassword)) {
            sikaUser.setPassword(SaSecureUtil.sha256(userPassword));
        }
        else {
            sikaUser.setPassword(SaSecureUtil.sha256(registerDTO.getPhone()));
        }

        /* 插入用户数据 */
        int insertResult = sikaUserMapper.insertUser(sikaUser);
        if (insertResult != 1) {
            throw new RegisterException("注册用户失败, 请联系管理人员!");
        }
        /* 生成角色信息 */
        SikaUserRole userRoleMapEntity = SikaUserRole.builder()
                .userId(sikaUser.getUserId())
                .roleId(AuthenticationConstant.USER_ROLE_ID)
                .build();
        int mapUserRoleResult = sikaUserRoleMapper.insert(userRoleMapEntity);
        if (mapUserRoleResult != 1) {
            throw new RegisterException("绑定用户角色失败, 请联系管理人员!");
        }
        return sikaUser.getNickname() + "用户注册成功";
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
