package cc.sika.bookkeeping.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果前端视图对象
 * @param <T>
 */
@Data
public class PageVO<T> implements Serializable {
    /**
     * 页码
     */
    private Long pageNum;
    /**
     * 每页条数
     */
    private Long pageSize;
    /**
     * 总条数
     */
    private Long totalCount;
    /**
     * 总页数
     */
    private Long totalPages;
    /**
     * 结果数据集
     */
    private List<T> list;
}
