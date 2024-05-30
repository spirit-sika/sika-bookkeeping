package cc.sika.bookkeeping.mapper;

import cc.sika.bookkeeping.pojo.po.SikaPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SikaPermissionMapper extends BaseMapper<SikaPermission> {
    List<String> getPermissionsByRoleId(@Param("roleId") Long roleId);
    List<String> getPermissionsByUserId(@Param("userId") Long userId);
}
