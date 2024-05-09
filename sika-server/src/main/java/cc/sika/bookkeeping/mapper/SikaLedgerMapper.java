package cc.sika.bookkeeping.mapper;

import cc.sika.bookkeeping.annotation.AutoFill;
import cc.sika.bookkeeping.constant.OperationType;
import cc.sika.bookkeeping.pojo.po.SikaLedger;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface SikaLedgerMapper extends BaseMapper<SikaLedger> {

    @AutoFill(OperationType.INSERT)
    int insertLeger(SikaLedger sikaLedger);
}
