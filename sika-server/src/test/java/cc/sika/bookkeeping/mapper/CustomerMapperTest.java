package cc.sika.bookkeeping.mapper;

import cc.sika.bookkeeping.pojo.po.SikaUser;
import cn.dev33.satoken.secure.SaSecureUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CustomerMapperTest {
    @Resource
    private SikaUserMapper sikaUserMapper;

    @Test
    void testInsertUser() {
        SikaUser testUser = SikaUser.builder()
                .username("单元测试用户1")
                .phone("13111111111")
                .password(SaSecureUtil.sha256("13111111111"))
                .email("13111111111@qq.com")
                .sex(Byte.valueOf("1"))
                .avatar("")
                .build();
        sikaUserMapper.insertUser(testUser);
    }
}
