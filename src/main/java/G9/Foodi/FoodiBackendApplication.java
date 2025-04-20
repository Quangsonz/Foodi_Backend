package G9.Foodi;

//import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class FoodiBackendApplication {
    private static final Logger logger = LoggerFactory.getLogger(FoodiBackendApplication.class);

    public static void main(String[] args) {
        // Đọc giá trị từ .env
//        Dotenv dotenv = Dotenv.load();
//        String mongoUri = dotenv.get("MONGODB_URI");
//        logger.info("MONGODB_URI from .env: {}", mongoUri);

        // Khởi động ứng dụng Spring Boot
        SpringApplication app = new SpringApplication(FoodiBackendApplication.class);
        Environment env = app.run(args).getEnvironment();

        // Log giá trị spring.data.mongodb.uri
        String springMongoUri = env.getProperty("spring.data.mongodb.uri");
        logger.info("spring.data.mongodb.uri: {}", springMongoUri);
    }
}