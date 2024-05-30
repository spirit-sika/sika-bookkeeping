package cc.sika.bookkeeping.service.impl;

import cc.sika.bookkeeping.StatusEnum;
import cc.sika.bookkeeping.constant.PublicFieldConstant;
import cc.sika.bookkeeping.mapper.SikaLedgerMapper;
import cc.sika.bookkeeping.pojo.dto.BaseQuery;
import cc.sika.bookkeeping.pojo.dto.LedgeQueryDTO;
import cc.sika.bookkeeping.pojo.po.SikaLedger;
import cc.sika.bookkeeping.pojo.vo.PageVO;
import cc.sika.bookkeeping.service.SikaLedgerService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

@Service("sikaLedgerService")
public class DefaultSikaLedgerServiceImpl extends ServiceImpl<SikaLedgerMapper, SikaLedger> implements SikaLedgerService {

    @Override
    @Cacheable(cacheNames = "ledgerCache", key = "#query.toString()")
    public PageVO<SikaLedger> getLedgersPage(BaseQuery<LedgeQueryDTO> query) {
        // 1. 构造查询条件
        fillQuery(query);
        LedgeQueryDTO data = query.getData();
        // 构建分页查询对象并根据query对象指定排序字段
        Page<SikaLedger> page = generatePageWithSortInfo(query);
        // 2. 分页查询
        if (Objects.isNull(data)) {
            page = lambdaQuery().page(page);
        }
        else {
            page = lambdaQuery()
                    .eq(!Objects.isNull(data.getLedgerId()), SikaLedger::getLedgerId, data.getLedgerId())
                    .eq(!Objects.isNull(data.getLedgerName()), SikaLedger::getLedgerName, data.getLedgerName())
                    .eq(!Objects.isNull(data.getStatus()), SikaLedger::getLedgerStatus, data.getStatus())
                    .eq(!Objects.isNull(data.getCreateTime()), SikaLedger::getCreateTime, data.getCreateTime())
                    .eq(!Objects.isNull(data.getUsername()), SikaLedger::getCreateBy, data.getUsername())
                    .page(page);
        }
        // 3. 封装VO
        return page2PageVO(query, page);
    }

    /**
     * 将 MP 的 Page 转为 PageVO, Page中没有数据则返回空集合
     * @param query 查询参数
     * @param page MP的查询结果Page对象
     * @return PageVO
     */
    private static <T> PageVO<T> page2PageVO(BaseQuery<?> query, Page<T> page) {
        PageVO<T> pageVO = new PageVO<>();
        pageVO.setTotalCount(page.getTotal());
        pageVO.setTotalPages(page.getPages());
        pageVO.setPageNum(Long.valueOf(query.getPageNum()));
        pageVO.setPageSize(Long.valueOf(query.getPageSize()));
        // 4. 返回
        List<T> records = page.getRecords();
        // 没有数据则返回空集合
        pageVO.setList(records);
        return pageVO;
    }

    /**
     * 对必要的查询参数进行自动填充
     * @param query 查询参数 BaseQuery 的子类
     */
    private <T> void fillQuery(BaseQuery<T> query) {
        /* 处理排序方式 */
        if (Objects.isNull(query.getIsDesc())) {
            query.setIsDesc(Boolean.FALSE);
        }

        /* 处理页码 */
        if (Objects.isNull(query.getPageNum()) || query.getPageNum() <= 0) {
            query.setPageNum(1);
        }
        /* 处理页容量 */
        if (Objects.isNull(query.getPageSize()) || query.getPageSize() <= 0) {
            query.setPageSize(10);
        }

        /* 校验状态字段 */
        T data = query.getData();
        if (Objects.isNull(query.getData())) {
            return ;
        }
        try {
            Field statusField = data.getClass().getDeclaredField("status");
            statusField.setAccessible(true);
            StatusEnum status = (StatusEnum) statusField.get(data);
            if (Objects.isNull(status)
                    || (status != StatusEnum.ENABLE
                    && status != StatusEnum.DISABLE)) {
                statusField.set(data, StatusEnum.ENABLE);
            }
        } catch (NoSuchFieldException | IllegalAccessException ignored) {}
    }

    /**
     * 构建分页查询对象并设置排序, 只填充页码, 尺寸以及排序信息, 未指定排序信息则按照更新时间
     * @param query
     * @return
     * @param <T> 分页查询的结果实体
     */
    private <T> Page<T> generatePageWithSortInfo(BaseQuery<?> query) {
        Page<T> page = Page.of(query.getPageNum(), query.getPageSize());

        // 排序字段, 排序顺序
        if (StrUtil.isNotBlank(query.getOrderBy())) {
            OrderItem orderItem = new OrderItem();
            orderItem.setColumn(query.getOrderBy()).setAsc(!query.getIsDesc());
            page.addOrder(orderItem);
        }
        // 未指定排序字段按照更新时间排序
        else {
            OrderItem orderItem = new OrderItem();
            orderItem.setColumn(PublicFieldConstant.UPDATE_BY)
                    .setAsc(!query.getIsDesc());
            page.addOrder(orderItem);
        }

        return page;
    }
}
