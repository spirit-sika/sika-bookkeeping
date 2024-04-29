package cc.sika.bookkeeping.pojo.dto;

import cc.sika.bookkeeping.constant.DeviceType;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginDTO implements Serializable {
    private String phone;
    private String password;
    private String codeKey;
    private String code;
    private DeviceType deviceType;
    private Boolean rememberMe;
}
