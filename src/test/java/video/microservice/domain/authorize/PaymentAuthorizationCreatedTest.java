package video.microservice.domain.authorize;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentAuthorizationCreatedTest {
    @Test
    public void shouldCreateObjectWithTrueFlag() {
        PaymentAuthorizationCreated authorization = PaymentAuthorizationCreated.builder()
                .data("payment123")
                .message("Authorization created")
                .isCreated(true)
                .build();

        assertEquals("payment123", authorization.getData());
        assertEquals("Authorization created", authorization.getMessage());
        assertTrue(authorization.isCreated());
    }

    @Test
    public void shouldCreateObjectWithFalseFlag() {
        PaymentAuthorizationCreated authorization = PaymentAuthorizationCreated.builder()
                .data("payment456")
                .message("Authorization failed")
                .isCreated(false)
                .build();

        assertEquals("payment456", authorization.getData());
        assertEquals("Authorization failed", authorization.getMessage());
        assertFalse(authorization.isCreated());
    }
}
