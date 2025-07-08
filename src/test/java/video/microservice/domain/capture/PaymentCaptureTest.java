package video.microservice.domain.capture;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentCaptureTest {
    @Test
    public void shouldCreateWithBuilder() {
        PaymentCapture capture = PaymentCapture.builder()
                .paymentId("abc-123")
                .status(PaymentStatus.COMPLETED)
                .build();

        assertEquals("abc-123", capture.getPaymentId());
        assertEquals(PaymentStatus.COMPLETED, capture.getStatus());
    }

    @Test
    public void shouldCreateWithAllArgs() {
        PaymentCapture capture = new PaymentCapture("abc-123", PaymentStatus.PENDING);

        assertEquals("abc-123", capture.getPaymentId());
        assertEquals(PaymentStatus.PENDING, capture.getStatus());
    }

}
