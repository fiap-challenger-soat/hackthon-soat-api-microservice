package video.microservice.infraestructure.adapters.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AwsConfigTest {

//    @Test
//    void shouldCreateAmazonSQSAsync() {
//        AwsConfig config = new AwsConfig();
//        config.setAccessKey("test-access-key");
//        config.setSecretKey("test-secret-key");
//        config.setRegion("us-east-1");
//        config.setSqsEndpoint("http://localhost:4566");
//
//        AmazonSQSAsync sqs = config.amazonSQSAsync();
//
//        assertNotNull(sqs);
//    }

    @Test
    void shouldCreateAmazonS3() {
        AwsConfig config = new AwsConfig();
        config.setAccessKey("test-access-key");
        config.setSecretKey("test-secret-key");
        config.setRegion("us-east-1");
        config.setS3Endpoint("http://localhost:4566");

        AmazonS3 s3 = config.amazonS3();

        assertNotNull(s3);
    }
}
