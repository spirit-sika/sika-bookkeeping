package cc.sika.bookkeeping.pojo.vo;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>用户登录成功后携带用户显示信息VO</p>
 * 包含以下信息:
 * <ul>
 *     <li>nickname</li>
 *     <li>sex</li>
 *     <li>avatar</li>
 *     <li>token</li>
 * </ul>
 *
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoginVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 显示名称 */
    private String nickname;
    /** 性别 0未知, 1男, 2女 */
    private Byte sex;
    /** 头像url */
    private String avatar;
    /** 角色名称 */
    private String role;
    /** token */
    private String token;
}
