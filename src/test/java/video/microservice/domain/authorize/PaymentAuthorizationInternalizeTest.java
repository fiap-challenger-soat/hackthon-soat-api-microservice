package video.microservice.domain.authorize;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentAuthorizationInternalizeTest {
    @Test
    public void shouldCreateWithBuilder() {
        PaymentAuthorizationInternalize internalize = PaymentAuthorizationInternalize.builder()
                .paymentId("abcd-123")
                .data("payment123")
                .status(PaymentStatus.COMPLETED)
                .build();

        assertEquals("abcd-123", internalize.paymentId());
        assertEquals("payment123", internalize.data());
        assertEquals(PaymentStatus.COMPLETED, internalize.status());
    }

    @Test
    public void shouldImplementEqualsAndHashCode() {
        PaymentAuthorizationInternalize obj1 = PaymentAuthorizationInternalize.builder()
                .paymentId("abc-123")
                .data("data123")
                .status(PaymentStatus.PENDING)
                .build();

        PaymentAuthorizationInternalize obj2 = PaymentAuthorizationInternalize.builder()
                .paymentId("abc-123")
                .data("data123")
                .status(PaymentStatus.PENDING)
                .build();

        assertEquals(obj1, obj2);
        assertEquals(obj1.hashCode(), obj2.hashCode());
    }

    @Test
    public void shouldImplementToString() {
        PaymentAuthorizationInternalize internalize = PaymentAuthorizationInternalize.builder()
                .paymentId("teste123")
                .data("data")
                .status(PaymentStatus.FAILED)
                .build();

        assertNotNull(internalize.toString());
        assertTrue(internalize.toString().contains("teste123"));
        assertTrue(internalize.toString().contains("data"));
    }
}
