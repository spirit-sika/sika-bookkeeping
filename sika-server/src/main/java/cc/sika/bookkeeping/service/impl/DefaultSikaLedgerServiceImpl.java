package cc.sika.bookkeeping.service.impl;

import cc.sika.bookkeeping.constant.PublicFieldNameConstant;
import cc.sika.bookkeeping.mapper.SikaLedgerMapper;
import cc.sika.bookkeeping.pojo.dto.BaseQuery;
import cc.sika.bookkeeping.pojo.dto.StatusBaseQuery;
import cc.sika.bookkeeping.pojo.po.SikaLedger;
import cc.sika.bookkeeping.pojo.vo.PageVO;
import cc.sika.bookkeeping.service.SikaLedgerService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service("sikaLedgerService")
@RequiredArgsConstructor
public class DefaultSikaLedgerServiceImpl extends ServiceImpl<SikaLedgerMapper, SikaLedger> implements SikaLedgerService {

    @Override
    @Cacheable(cacheNames = "ledgerCache", key = "#statusQuery.toString()")
    public PageVO<SikaLedger> getLedgersPage(StatusBaseQuery statusQuery) {
        // 1. 构造查询条件
        checkQuery(statusQuery);
        Byte status = statusQuery.getStatus();
        Page<SikaLedger> page = Page.of(statusQuery.getPageNum(), statusQuery.getPageSize());
        // 按照指定的字段排序
        if (StrUtil.isNotBlank(statusQuery.getOrderBy())) {
            OrderItem orderItem = new OrderItem();
            orderItem.setColumn(statusQuery.getOrderBy())
                            .setAsc(!statusQuery.getIsDesc());
            page.addOrder(orderItem);
        }
        // 按照更新时间排序
        else {
            OrderItem orderItem = new OrderItem();
            orderItem.setColumn(PublicFieldNameConstant.UPDATE_BY)
                    .setAsc(!statusQuery.getIsDesc());
            page.addOrder(orderItem);
        }
        // 2. 分页查询
        Page<SikaLedger> sikaLedgerPage = lambdaQuery()
                .eq(!Objects.isNull(status), SikaLedger::getLedgerStatus, status)
                .page(page);

        // 3. 封装VO
        PageVO<SikaLedger> sikaLedgerPageVO = new PageVO<>();
        sikaLedgerPageVO.setTotalCount(sikaLedgerPage.getTotal());
        sikaLedgerPageVO.setTotalPages(sikaLedgerPage.getPages());
        sikaLedgerPageVO.setPageNum(Long.valueOf(statusQuery.getPageNum()));
        sikaLedgerPageVO.setPageSize(Long.valueOf(statusQuery.getPageSize()));
        // 4. 返回
        List<SikaLedger> records = sikaLedgerPage.getRecords();
        // 没有数据则返回空集合
        if (CollUtil.isEmpty(records)) {
            sikaLedgerPageVO.setList(Collections.emptyList());
        }
        else {
            sikaLedgerPageVO.setList(records);
        }
        return sikaLedgerPageVO;
    }

    private <T extends BaseQuery> void checkQuery(T query) {
        /* 处理排序方式 */
        if (Objects.isNull(query.getIsDesc())) {
            query.setIsDesc(Boolean.FALSE);
        }
        /* 处理状态值 */
        if (query instanceof StatusBaseQuery statusQuery) {
            statusQuery.setStatus((byte)2);
            if (Objects.isNull(statusQuery.getStatus())
                    || statusQuery.getStatus() != 0
                    && statusQuery.getStatus() != 1) {
                statusQuery.setStatus((byte) 0);
            }
        }

        /* 处理页码 */
        if (Objects.isNull(query.getPageNum()) || query.getPageNum() <= 0) {
            query.setPageNum(1);
        }
        /* 处理页容量 */
        if (Objects.isNull(query.getPageSize()) || query.getPageSize() <= 0) {
            query.setPageSize(10);
        }

    }

    private final SikaLedgerMapper sikaLedgerMapper;
}
