package cc.sika.bookkeeping.pojo.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 账本实体
 */
@Data
@Builder
@TableName("sika_ledger")
public class SikaLedger implements Serializable {
    /**
     * 账本id
     */
    @TableId
    private Long ledgerId;

    /**
     * 账本名称
     */
    private String ledgerName;

    /**
     * 账本状态, 0启用, 1冻结(删除)
     */
    private Byte ledgerStatus;

    /**
     * 账本创建人
     */
    private String createBy;
    /**
     * 账本创建时间
     */
    private LocalDateTime createTime;
    /**
     * 账本更新人
     */
    private String updateBy;
    /**
     * 账本更新时间
     */
    private LocalDateTime updateTime;
}
