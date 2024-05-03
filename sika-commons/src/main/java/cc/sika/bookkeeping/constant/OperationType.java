package cc.sika.bookkeeping.constant;

/**
 * 与@AutoFill注解搭配使用的操作类型枚举,
 * 对应数据库操作类型
 * <ul>
 *     <li>INSERT, 插入数据操作</li>
 *     <li>UPDATE, 更新数据操作</li>
 *     <li>DELETE, 删除数据操作</li>
 * </ul>
 */
public enum OperationType {
    INSERT, UPDATE, DELETE
}
