package video.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {
        "video.microservice",
        "video.microservice.infraestructure.adapters.aws"
})
public class VideoApplication {
    public static void main(String[] args) {
       SpringApplication.run(VideoApplication.class, args);
    }
}