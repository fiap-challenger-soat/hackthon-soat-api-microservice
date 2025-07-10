package video.microservice.infraestructure.adapters.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import video.microservice.infraestructure.adapters.process.OutVideoProcessAwsImpl;
import payment.microservice.domain.authorize.PaymentAuthorizationInternalize;
import payment.microservice.domain.capture.PaymentCaptureInternalize;
import payment.microservice.domain.capture.PaymentCapture;
import payment.microservice.infraestructure.adapters.rest.request.NotificationRequest;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OutVideoProcessAwsImpl videoService;
    @InjectMocks
    private VideoApiController videoApiController;


    private ObjectMapper objectMapper;

    private Payment samplePayment;
    private NotificationRequest sampleNotification;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        samplePayment = Payment.builder()
                .id("payment123")
                .orderId("order123")
                .amount(new BigDecimal("150.00"))
                .currency("BRL")
                .status(PaymentStatus.PENDING)
                .method(PaymentMethod.PIX)
                .quantity(1)
                .build();

        sampleNotification = new NotificationRequest();
        mockMvc = MockMvcBuilders.standaloneSetup(videoApiController)
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }, "/*")
                .build();
    }

    @Test
    void shouldReturnOkAuthorizeEndpoint() throws Exception {
        PaymentAuthorizationInternalize authorizationResponse = PaymentAuthorizationInternalize.builder()
                .data("authorized")
                .paymentId("abc-123")
                .status(PaymentStatus.PENDING)
                .build();

        when(videoService.processAuthorize(any(Payment.class))).thenReturn(authorizationResponse);

        mockMvc.perform(post("/v1/api/payment/authorize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(samplePayment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("authorized"))
                .andExpect(jsonPath("$.paymentId").value("abc-123"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void shouldReturnOkHandleEndpoint() throws Exception {
        PaymentCaptureInternalize captureResponse = PaymentCaptureInternalize.builder()
                .paymentId("payment123")
                .status(PaymentStatus.PENDING)
                .build();

        when(videoService.processCapture(any(PaymentCapture.class))).thenReturn(captureResponse);

        mockMvc.perform(post("/v1/api/payment/handle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleNotification)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnOkFindByIdEndpoint() throws Exception {
        when(videoService.findPaymentById("payment123")).thenReturn(samplePayment);

        mockMvc.perform(get("/v1/api/payment/payment123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("payment123"))
                .andExpect(jsonPath("$.orderId").value("order123"))
                .andExpect(jsonPath("$.amount").value(150.00));
    }
}
