package cc.sika.bookkeeping.service;

import cc.sika.bookkeeping.pojo.dto.BaseQuery;
import cc.sika.bookkeeping.pojo.dto.LedgeQueryDTO;
import cc.sika.bookkeeping.pojo.po.SikaLedger;
import cc.sika.bookkeeping.pojo.vo.PageVO;
import com.baomidou.mybatisplus.extension.service.IService;


public interface SikaLedgerService extends IService<SikaLedger> {

    /**
     * 分页获取当前用户的所有账本
     * @return 账本列表分页实体
     */
    PageVO<SikaLedger> getLedgersPage(BaseQuery<LedgeQueryDTO> query);
}
