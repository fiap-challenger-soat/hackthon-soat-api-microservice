package video.microservice.infraestructure.adapters.aws.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import video.microservice.domain.VideoSqs;

@Component
@Slf4j
public class SqsMessageSender {

    @Autowired
    private AmazonSQS amazonSQS;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendMessage(String messageBody, String queueName) {
        log.info("Queue name: {}", queueName);

        String queueUrl = amazonSQS.getQueueUrl(queueName).getQueueUrl();
        log.info("Resolved queue URL: {}", queueUrl);

        VideoSqs bodyDto = new VideoSqs(messageBody);

        try {
            String messageBodyJson = objectMapper.writeValueAsString(bodyDto);
            amazonSQS.sendMessage(new SendMessageRequest(queueUrl, messageBodyJson));
            log.info("Mensagem enviada para a fila {}: {}", queueName, messageBodyJson);
        } catch (JsonProcessingException e) {
            log.error("Erro ao serializar a mensagem para JSON", e);
            throw new RuntimeException("Erro ao enviar mensagem para a fila SQS", e);
        }
    }
}