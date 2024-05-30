package cc.sika.bookkeeping.mapper;

import cc.sika.bookkeeping.pojo.po.SikaRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SikaRoleMapper extends BaseMapper<SikaRole> {
    List<String> getRolesByUserId(@Param("userId") Long userId);
}
