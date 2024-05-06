package cc.sika.bookkeeping.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 账单条目实体
 */
@Data
@Builder
@TableName("sika_record")
public class SikaRecord implements Serializable {
    /**
     * 账单条目id
     */
    private Long recordId;

    /**
     * 账单条目名称
     */
    private String recordName;

    /**
     * 账单条目类型, 0收入, 1支出
     */
    private Byte recordType;

    /**
     * 账单条目时间
     */
    private LocalDateTime recordTime;

    /**
     * 账单条目金额, 单位毫, 换算单位: 元, 角, 分, 厘, 毫
     */
    private Long recordAmount;

    /**
     * 账单条目状态, 0启用, 1删除
     */
    private Byte recordStatus;

    /**
     * 对应账本id
     */
    private Long ledgerId;

    /**
     * 账单条目创建人
     */
    private String createBy;
    /**
     * 账单条目创建时间
     */
    private LocalDateTime createTime;
    /**
     * 账单条目更新人
     */
    private String updateBy;
    /**
     * 账单条目更新时间
     */
    private String updateTime;
}
