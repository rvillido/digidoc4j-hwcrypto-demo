package ee.sk.hwcrypto.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class DssHwcryptoWebappApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DssHwcryptoWebappApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(DssHwcryptoWebappApplication.class, args);
    }
}
