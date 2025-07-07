package payment.microservice.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentTest {
    @Test
    public void shouldCreateObjectBuilder() {
        Payment payment = Payment.builder()
                .id("pmt123")
                .method(PaymentMethod.CREDIT)
                .orderId("order789")
                .amount(new BigDecimal("99.99"))
                .currency("USD")
                .status(PaymentStatus.PENDING)
                .quantity(2)
                .build();

        assertEquals("pmt123", payment.getId());
        assertEquals(PaymentMethod.CREDIT, payment.getMethod());
        assertEquals("order789", payment.getOrderId());
        assertEquals(new BigDecimal("99.99"), payment.getAmount());
        assertEquals("USD", payment.getCurrency());
        assertEquals(PaymentStatus.PENDING, payment.getStatus());
        assertEquals(2, payment.getQuantity());
    }

    @Test
    public void shouldCreateObjectAllArgsConstructor() {
        Payment payment = new Payment("pmt456", PaymentMethod.PIX, "order321", new BigDecimal("150.00"),
                "BRL", PaymentStatus.COMPLETED, 1);

        assertEquals("pmt456", payment.getId());
        assertEquals(PaymentMethod.PIX, payment.getMethod());
        assertEquals("order321", payment.getOrderId());
        assertEquals(new BigDecimal("150.00"), payment.getAmount());
        assertEquals("BRL", payment.getCurrency());
        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
        assertEquals(1, payment.getQuantity());
    }
}
