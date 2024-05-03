package cc.sika.bookkeeping;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTest {
    @Test
    void testRedisConnect(@Autowired RedisTemplate<String, String> redisTemplate) {
        redisTemplate.opsForValue().set("testKey", "testValue");
    }
}
