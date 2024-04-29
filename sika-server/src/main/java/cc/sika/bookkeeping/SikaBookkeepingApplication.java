package cc.sika.bookkeeping;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cc.sika.bookkeeping.mapper")
public class SikaBookkeepingApplication {
    public static void main(String[] args) {
        SpringApplication.run(SikaBookkeepingApplication.class, args);
    }
}
