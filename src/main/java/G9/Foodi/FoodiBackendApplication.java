package G9.Foodi; 

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import G9.Foodi.config.JwtConfig;

@SpringBootApplication
@EnableConfigurationProperties(JwtConfig.class)
public class FoodiBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoodiBackendApplication.class, args);
    }
}

