package cc.sika.bookkeeping.service;

import cc.sika.bookkeeping.constant.LedgerConstant;
import cc.sika.bookkeeping.pojo.po.SikaLedger;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class MPServiceSaveTest {

    @Resource
    private SikaLedgerService sikaLedgerService;

    /**
     * 测试MP的save能否获取出自增主键
     */
    @Test
    void testSaveCanGenerateId() {
        LocalDateTime now = LocalDateTime.now();
        SikaLedger ledger = SikaLedger.builder()
                .ledgerName(LedgerConstant.DEFAULT_LEDGER_NAME)
                .ledgerStatus((byte) 0)
                .createBy("小吴来哩")
                .createTime(now)
                .updateBy("小吴来哩")
                .updateTime(now)
                .build();
        sikaLedgerService.save(ledger);
        System.out.println("ledger's id = " + ledger.getLedgerId());
    }
}
