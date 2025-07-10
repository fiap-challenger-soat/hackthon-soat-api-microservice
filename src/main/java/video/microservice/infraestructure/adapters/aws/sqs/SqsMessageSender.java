package video.microservice.infraestructure.adapters.aws.sqs;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SqsMessageSender {
    @Autowired
    private AmazonSQSAsync amazonSQS;

    public void sendMessage(String messageBody, String queueName) {
        String queueUrl = amazonSQS.getQueueUrl(queueName).getQueueUrl();

        amazonSQS.sendMessage(new SendMessageRequest(queueUrl, messageBody));

       log.info("Mensagem enviada para a fila {}", queueName);
    }
}
