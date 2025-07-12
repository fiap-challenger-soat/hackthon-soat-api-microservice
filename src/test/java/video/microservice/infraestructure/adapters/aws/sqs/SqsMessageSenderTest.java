package video.microservice.infraestructure.adapters.aws.sqs;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import video.microservice.domain.VideoSqs;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SqsMessageSenderTest {
    @Mock
    private AmazonSQSAsync amazonSQS;
    @InjectMocks
    private SqsMessageSender sqsMessageSender;
    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testShouldSendQueue() throws JsonProcessingException {
        String messageBody = "videoDigimon.mp4";
        String queueName = "work-queue";
        String queueUrl = "http://localhost:4566/000000000000/minha-fila";

        when(amazonSQS.getQueueUrl(queueName)).thenReturn(new GetQueueUrlResult().withQueueUrl(queueUrl));
        String messageBodyJson = "{\"message\":\"videoDigimon.mp4\"}";
        when(objectMapper.writeValueAsString(any(VideoSqs.class))).thenReturn(messageBodyJson);

        sqsMessageSender.sendMessage(messageBody, queueName);

        ArgumentCaptor<SendMessageRequest> captor = ArgumentCaptor.forClass(SendMessageRequest.class);
        verify(amazonSQS).sendMessage(captor.capture());

        SendMessageRequest request = captor.getValue();
        assertEquals(queueUrl, request.getQueueUrl());
        assertEquals(messageBodyJson, request.getMessageBody());
    }

    @Test
    void testShoulThrowException() throws JsonProcessingException {
        String messageBody = "videoDigimon.mp4";
        String queueName = "error-queue";

        when(amazonSQS.getQueueUrl(queueName)).thenReturn(new GetQueueUrlResult().withQueueUrl("work-queue"));
        when(objectMapper.writeValueAsString(any(VideoSqs.class)))
                .thenThrow(new JsonProcessingException("Serialization error"){});

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sqsMessageSender.sendMessage(messageBody, queueName);
        });

        assertTrue(exception.getMessage().contains("Erro ao enviar mensagem para a fila SQS"));
    }
}
