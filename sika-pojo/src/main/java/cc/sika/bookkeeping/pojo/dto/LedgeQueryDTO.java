package cc.sika.bookkeeping.pojo.dto;

import cc.sika.bookkeeping.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 账本id, 账本名称, 账本状态, 时间, 用户id, 用户名
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LedgeQueryDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long ledgerId;
    private String ledgerName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String username;
    private StatusEnum status;

}
