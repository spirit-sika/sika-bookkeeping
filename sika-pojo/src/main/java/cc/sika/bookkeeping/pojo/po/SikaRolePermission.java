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
@TableName("sika_role_permission")
public class SikaRolePermission  implements Serializable {

    private Long roleId;
    private Long permissionId;
}
