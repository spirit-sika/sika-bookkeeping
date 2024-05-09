package cc.sika.bookkeeping.service;

import cc.sika.bookkeeping.pojo.dto.LoginDTO;
import cc.sika.bookkeeping.pojo.dto.RegisterDTO;
import cc.sika.bookkeeping.pojo.po.SikaUser;
import cc.sika.bookkeeping.pojo.vo.LoginVO;
import com.baomidou.mybatisplus.extension.service.IService;

public interface LoginService extends IService<SikaUser> {

    /**
     * 用户登录方法
     * @param loginDTO 用户登录表单信息, 包含设备类型
     * @return 登录成功返回Token字符串
     */
    String login(LoginDTO loginDTO);

    LoginVO loginAndGetVO(LoginDTO loginDTO);

    String register(RegisterDTO registerDTO);
}
