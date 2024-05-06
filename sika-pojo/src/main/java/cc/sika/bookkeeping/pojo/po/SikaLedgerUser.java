package cc.sika.bookkeeping.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@TableName("sika_ledger_user")
public class SikaLedgerUser implements Serializable {
    private Long userId;
    private Long ledgerId;
}
