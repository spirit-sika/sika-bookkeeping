package cc.sika.bookkeeping;

import cn.dev33.satoken.secure.SaSecureUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PasswordGeneratorTest {
    @Test
    void testGeneratePassword() {
        // 8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92
        String sha256 = SaSecureUtil.sha256("123456");
        System.out.println("sha256 = " + sha256);

        // 160ac3938a33fbeb01e74f414dc0c7ec6c1515f415321335b6f9258f5108acfa
        String wjc52292 = SaSecureUtil.sha256("wjc52292");
        System.out.println("wjc52292 = " + wjc52292);
    }
}
