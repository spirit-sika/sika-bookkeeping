package cc.sika.bookkeeping.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("sika_user_role")
public class SikaUserRole  implements Serializable {

    private Long userId;
    private Long roleId;
}
