package payment.microservice.infraestructure.adapters.database.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentEntityTest {
    @Test
    public void shouldCreateWithBuilçder() {
        LocalDateTime now = LocalDateTime.now();

        PaymentEntity entity = PaymentEntity.builder()
                .id("entity123")
                .amount(new BigDecimal("250.00"))
                .createdAt(now)
                .finishedAt(now.plusHours(1))
                .paymentMethod("CREDIT")
                .orderId("order456")
                .status("COMPLETED")
                .build();

        assertEquals("entity123", entity.getId());
        assertEquals(new BigDecimal("250.00"), entity.getAmount());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(now.plusHours(1), entity.getFinishedAt());
        assertEquals("CREDIT", entity.getPaymentMethod());
        assertEquals("order456", entity.getOrderId());
        assertEquals("COMPLETED", entity.getStatus());
    }

    @Test
    public void shouldCreateWithAllArgs(){
    LocalDateTime now = LocalDateTime.now();

    PaymentEntity entity = new PaymentEntity("entity999",new BigDecimal("100.00"),
                now, now.plusMinutes(30), "DEBIT", "order789", "PENDING");

        assertEquals("entity999", entity.getId());
        assertEquals(new BigDecimal("100.00"), entity.getAmount());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(now.plusMinutes(30), entity.getFinishedAt());
        assertEquals("DEBIT", entity.getPaymentMethod());
        assertEquals("order789", entity.getOrderId());
        assertEquals("PENDING", entity.getStatus());
    }
}
