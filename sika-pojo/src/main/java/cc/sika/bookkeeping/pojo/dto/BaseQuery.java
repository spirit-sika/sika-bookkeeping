package cc.sika.bookkeeping.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通用分页查询参数接收类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseQuery implements Serializable {
    /**
     * 页数
     */
    private Integer pageNum;
    /**
     * 每页条数
     */
    private Integer pageSize;
    /**
     * 按照哪个字段查询
     */
    private String orderBy;
    /**
     * 是否降序排列, 默认升序
     */
    private Boolean isDesc;
}
