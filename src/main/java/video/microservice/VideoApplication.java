package payment.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication(
        exclude = {
                DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class
        }
)
@EnableMongoAuditing
public class PaymentsApplication {
    public static void main(String[] args) {
       SpringApplication.run(PaymentsApplication.class, args);
    }
}