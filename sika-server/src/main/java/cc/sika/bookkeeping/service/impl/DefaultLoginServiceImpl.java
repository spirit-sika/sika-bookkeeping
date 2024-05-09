package cc.sika.bookkeeping.service.impl;

import cc.sika.bookkeeping.constant.AuthenticationConstant;
import cc.sika.bookkeeping.constant.AutoFillConstant;
import cc.sika.bookkeeping.constant.LedgerConstant;
import cc.sika.bookkeeping.exception.LedgerException;
import cc.sika.bookkeeping.exception.LoginException;
import cc.sika.bookkeeping.exception.RegisterException;
import cc.sika.bookkeeping.mapper.SikaLedgerMapper;
import cc.sika.bookkeeping.mapper.SikaLedgerUserMapper;
import cc.sika.bookkeeping.mapper.SikaUserMapper;
import cc.sika.bookkeeping.mapper.SikaUserRoleMapper;
import cc.sika.bookkeeping.pojo.dto.LoginDTO;
import cc.sika.bookkeeping.pojo.dto.RegisterDTO;
import cc.sika.bookkeeping.pojo.po.SikaLedger;
import cc.sika.bookkeeping.pojo.po.SikaLedgerUser;
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
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service("loginService")
@RequiredArgsConstructor
public class DefaultLoginServiceImpl extends ServiceImpl<SikaUserMapper, SikaUser> implements LoginService  {

    private final CaptchaService captchaService;
    private final SikaUserService userService;
    private final SikaUserRoleMapper sikaUserRoleMapper;
    private final SikaUserMapper sikaUserMapper;
    private final SikaLedgerMapper sikaLedgerMapper;
    private final SikaLedgerUserMapper sikaLedgerUserMapper;

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
        /* 检查是否存在相同手机号码 */
        SikaUser isUserExists = sikaUserMapper.selectOne(
                lambdaQuery().eq(SikaUser::getPhone, registerDTO.getPhone()),
                false);
        if (!Objects.isNull(isUserExists)) {
            throw RegisterException.PHONE_EXITS();
        }


        /* 插入用户数据 */
        // 构建用户实体
        SikaUser sikaUser = buildSikaUser(registerDTO);
        int insertResult = sikaUserMapper.insertUser(sikaUser);
        if (insertResult != 1) {
            throw RegisterException.CREATE_ERROR();
        }

        // 绑定角色信息
        SikaUserRole userRoleMapEntity = SikaUserRole.builder()
                .userId(sikaUser.getUserId())
                .roleId(AuthenticationConstant.USER_ROLE_ID)
                .build();
        int mapUserRoleResult = sikaUserRoleMapper.insert(userRoleMapEntity);
        if (mapUserRoleResult != 1) {
            throw RegisterException.FAIL_BIND_ROLE();
        }

        // 创建默认账本
        SikaLedger sikaLedger = SikaLedger.builder()
                .ledgerName(LedgerConstant.DEFAULT_LEDGER_NAME)
                .ledgerStatus(AutoFillConstant.ENABLE_STATUS)
                .build();
        int legerInserted = sikaLedgerMapper.insertLeger(sikaLedger);
        if (legerInserted != 1) {
            throw LedgerException.CREATE_ERROR();
        }
        // 绑定账本
        SikaLedgerUser ledgerUser = SikaLedgerUser.builder()
                .userId(sikaUser.getUserId())
                .ledgerId(sikaLedger.getLedgerId())
                .build();
        int bindingLedger = sikaLedgerUserMapper.insert(ledgerUser);
        if (bindingLedger != 1) {
            throw LedgerException.FAIL_BIND_USER();
        }

        return sikaUser.getNickname() + "用户注册成功";
    }

    /**
     * 构建用户实体, 不合法数据使用默认值填充
     * @param registerDTO registerDTO 用户表单信息实体
     */
    private static SikaUser buildSikaUser(RegisterDTO registerDTO) {
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
        return sikaUser;
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
