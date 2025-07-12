// package video.microservice.infraestructure.adapters.aws.sqs;

// import com.amazonaws.services.sqs.AmazonSQSAsync;
// import com.amazonaws.services.sqs.model.GetQueueUrlResult;
// import com.amazonaws.services.sqs.model.SendMessageRequest;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

// public class SqsMessageSenderTest {
//     @Mock
//     private AmazonSQSAsync amazonSQS;
//     @InjectMocks
//     private SqsMessageSender sqsMessageSender;

//     @BeforeEach
//     public void setup(){
//         MockitoAnnotations.openMocks(this);
//     }
//     @Test
//     public void testSqsMessage(){
//         String queueName = "video-queue";
//         String queueUrl = "http://localhost:4566/000000000000/video-queue";
//         String messageBody = "test-message";

//         GetQueueUrlResult result = new GetQueueUrlResult().withQueueUrl(queueUrl);
//         when(amazonSQS.getQueueUrl(queueName)).thenReturn(result);

//         sqsMessageSender.sendMessage(messageBody, queueName);

//         verify(amazonSQS).getQueueUrl(queueName);
//         verify(amazonSQS).sendMessage(new SendMessageRequest(queueUrl, messageBody));
//     }
// }
