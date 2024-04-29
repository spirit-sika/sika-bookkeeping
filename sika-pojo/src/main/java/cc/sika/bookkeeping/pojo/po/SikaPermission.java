package cc.sika.bookkeeping.pojo.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@TableName("sika_permission")
public class SikaPermission implements Serializable {
    /* permission id, automatic increment */
    @TableId
    private Long permissionId;

    /* permission content string, like systemName:moduleName:buttonName */
    private String permissionContent;
}
