package cc.sika.bookkeeping.pojo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterDTO implements Serializable {

    @NotNull(message = "手机号码不能为空")
    private String phone;
    private String password;
    private Byte sex;
    @Email
    private String email;
    @NotNull(message = "用户昵称不能为空")
    private String nickname;
}
