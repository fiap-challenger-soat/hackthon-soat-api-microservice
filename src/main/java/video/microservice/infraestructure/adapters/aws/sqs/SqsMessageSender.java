package video.microservice.infraestructure.adapters.aws.sqs;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
