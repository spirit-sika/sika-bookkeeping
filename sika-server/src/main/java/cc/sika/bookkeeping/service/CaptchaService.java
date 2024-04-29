package cc.sika.bookkeeping.service;

import cc.sika.bookkeeping.pojo.vo.CaptchaVO;

public interface CaptchaService {

    /**
     * 生成验证码, 使用雪花算法生成验证码的键并将验证码放入到redis中设置有效时长
     * @return 返回带有验证码键值对的VO实体, 包含验证码的可寻键
     */
    CaptchaVO generateCaptcha();

    boolean checkCaptcha(String key, String code);
}
