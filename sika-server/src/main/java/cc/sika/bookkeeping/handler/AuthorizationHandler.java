package cc.sika.bookkeeping.handler;

import cc.sika.bookkeeping.mapper.SikaPermissionMapper;
import cc.sika.bookkeeping.mapper.SikaRoleMapper;
import cn.dev33.satoken.stp.StpInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthorizationHandler implements StpInterface {

    private final SikaPermissionMapper sikaPermissionMapper;
    private final SikaRoleMapper sikaRoleMapper;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return sikaPermissionMapper.getPermissionsByUserId(Long.valueOf(loginId.toString()));
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return sikaRoleMapper.getRolesByUserId(Long.valueOf(loginId.toString()));
    }
}
