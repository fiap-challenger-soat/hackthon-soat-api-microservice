package video.microservice.infraestructure.adapters.aws.sqs;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import video.microservice.infraestructure.adapters.aws.AwsConfig;

@Component
public class SqsMessageSender {
    @Autowired
    private AmazonSQSAsync amazonSQS;

    public void sendMessage(String messageBody, String queueName) {
        String queueUrl = amazonSQS.getQueueUrl(queueName).getQueueUrl();

        amazonSQS.sendMessage(new SendMessageRequest(queueUrl, messageBody));

        System.out.println("Mensagem enviada para a fila " + queueName);
    }
}
