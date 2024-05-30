package cc.sika.bookkeeping.mapper;

import cc.sika.bookkeeping.annotation.AutoFill;
import cc.sika.bookkeeping.constant.OperationType;
import cc.sika.bookkeeping.pojo.po.SikaUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface SikaUserMapper extends BaseMapper<SikaUser> {

    @AutoFill(OperationType.INSERT)
    int insertUser(SikaUser user);

    String selectRoleNameByUserId(@Param("userId") Long userId);
}
