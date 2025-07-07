package payment.microservice.application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpServerErrorException;
import payment.microservice.domain.authorize.PaymentAuthorizationCreated;
import payment.microservice.domain.authorize.PaymentAuthorizationInternalize;
import payment.microservice.domain.capture.PaymentCapture;
import payment.microservice.domain.capture.PaymentCaptureInternalize;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PaymentServiceTest {

    @Mock
    private PaymentEvents paymentEvents;
    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private VideoService videoService;

    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    public void shouldAuthorizeAndSavePaymentPending(){
        Mockito.when(paymentEvents.authorize(any())).thenReturn(PaymentAuthorizationCreated.builder()
                .data("data").message("message").isCreated(Boolean.TRUE).build());
        Payment payment = Payment.builder().id("abcde-123").orderId("fghi-123").method(PaymentMethod.CREDIT).amount(BigDecimal.TEN)
                        .currency("USD").quantity(10).build();
        PaymentAuthorizationInternalize paymentAuthorizationInternalize = videoService.processAuthorize(payment);
        verify(paymentRepository, times(1)).save(any(Payment.class));
        Assertions.assertEquals(PaymentStatus.PENDING, paymentAuthorizationInternalize.status());
        Assertions.assertNotNull(paymentAuthorizationInternalize);
    }

    @Test
    public void shouldAuthorizeAndSavePaymentFailed(){
        Mockito.when(paymentEvents.authorize(any())).thenReturn(PaymentAuthorizationCreated.builder()
                .data("data").message("message").isCreated(Boolean.FALSE).build());
        Payment payment = Payment.builder().id("abcde-123").orderId("fghi-123").method(PaymentMethod.DEBIT).amount(BigDecimal.TEN)
                .currency("USD").quantity(10).build();
        PaymentAuthorizationInternalize paymentAuthorizationInternalize = videoService.processAuthorize(payment);
        verify(paymentRepository, times(1)).save(any(Payment.class));
        Assertions.assertEquals(PaymentStatus.FAILED, paymentAuthorizationInternalize.status());
        Assertions.assertNotNull(paymentAuthorizationInternalize);
    }

    @Test
    public void shouldHaveErrorWhenAuthorizePayment(){
        Mockito.when(paymentEvents.authorize(any())).thenThrow(new HttpServerErrorException(HttpStatusCode.valueOf(500), "Internal Server Error"));
        Payment payment = Payment.builder().id("abcde-123").orderId("fghi-123").method(PaymentMethod.DEBIT).amount(BigDecimal.TEN)
                .currency("USD").quantity(10).build();
        PaymentAuthorizationInternalize paymentAuthorizationInternalize = videoService.processAuthorize(payment);
        verify(paymentRepository, never()).save(any(Payment.class));
        Assertions.assertEquals(PaymentStatus.FAILED, paymentAuthorizationInternalize.status());
    }

    @Test
        public void shouldProcessCapture(){
        PaymentCapture paymentCapture =  PaymentCapture.builder().paymentId("abcd-123").status(PaymentStatus.PENDING).build();

        PaymentCaptureInternalize paymentCaptureInternalize = videoService.processCapture(paymentCapture);
        verify(paymentRepository, times(1)).updateStatus(any(), any());
        Assertions.assertEquals(PaymentStatus.PENDING, paymentCaptureInternalize.status());
        Assertions.assertEquals("abcd-123", paymentCaptureInternalize.paymentId());
    }

    @Test
    public void shouldFailUpdateProcessCapture(){
        doThrow(new RuntimeException("internal server error")).when(paymentRepository).updateStatus(any(), any());
        PaymentCapture paymentCapture =  PaymentCapture.builder().paymentId("abcd-123").status(PaymentStatus.FAILED).build();

        PaymentCaptureInternalize paymentCaptureInternalize = videoService.processCapture(paymentCapture);
        Assertions.assertEquals(PaymentStatus.FAILED, paymentCaptureInternalize.status());
        Assertions.assertEquals("abcd-123", paymentCaptureInternalize.paymentId());
    }

    @Test
    public void shouldFindPaymentById(){
        Mockito.when(paymentRepository.findById(any())).thenReturn(Payment.builder().id("abcde-123").orderId("fghi-123").method(PaymentMethod.DEBIT).amount(BigDecimal.TEN)
                .currency("USD").quantity(10).build());
        Payment payment = videoService.findPaymentById("123");

        Assertions.assertEquals("abcde-123", payment.getId());
        Assertions.assertEquals("fghi-123", payment.getOrderId());
        Assertions.assertEquals(PaymentMethod.DEBIT, payment.getMethod());
        Assertions.assertEquals(BigDecimal.TEN, payment.getAmount());
        Assertions.assertEquals("USD", payment.getCurrency());
    }

    @Test
    public void shouldFailFindPaymentById(){
        Mockito.when(paymentRepository.findById(any())).thenThrow(new RuntimeException("Internal Server Error"));
        Payment payment = videoService.findPaymentById("123");
        Assertions.assertNull(payment);
    }
}
