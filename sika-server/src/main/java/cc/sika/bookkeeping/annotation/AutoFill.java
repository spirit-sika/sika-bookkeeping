package cc.sika.bookkeeping.annotation;

import cc.sika.bookkeeping.constant.OperationType;

import java.lang.annotation.*;

/**
 * 字段自动填充注解, 标注在mapper接口上将根据数据进行自动填充
 * <pre>
 *     &#064;AutoFill(OperationType.INSERT)
 *     void insert(PO po);
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoFill {
    OperationType value();
}
