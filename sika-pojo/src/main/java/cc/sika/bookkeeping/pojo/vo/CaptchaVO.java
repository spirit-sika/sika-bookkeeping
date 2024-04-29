package cc.sika.bookkeeping.pojo.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CaptchaVO {
    private String codeKey;
    private String base64;
}
