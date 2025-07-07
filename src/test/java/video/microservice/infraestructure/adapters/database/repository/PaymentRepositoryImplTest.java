package payment.microservice.infraestructure.adapters.database.repository;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentRepositoryImplTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private PaymentRepositoryImpl paymentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveData() {
        Payment payment = Payment.builder()
                .id("pay001")
                .orderId("order001")
                .method(PaymentMethod.PIX)
                .amount(new BigDecimal("100.00"))
                .status(PaymentStatus.PENDING)
                .build();

        paymentRepository.save(payment);

        ArgumentCaptor<PaymentEntity> argumentCaptor = ArgumentCaptor.forClass(PaymentEntity.class);
        verify(mongoTemplate, times(1)).save(argumentCaptor.capture());

        PaymentEntity entity = argumentCaptor.getValue();

        assertEquals("pay001", entity.getId());
        assertEquals("order001", entity.getOrderId());
        assertEquals("PIX", entity.getPaymentMethod());
        assertEquals(new BigDecimal("100.00"), entity.getAmount());
        assertEquals("PENDING", entity.getStatus());
        assertNull(entity.getFinishedAt());
    }

    @Test
    void shouldUpdateStatus() {
        String paymentId = "pay002";
        PaymentStatus newStatus = PaymentStatus.COMPLETED;

        paymentRepository.updateStatus(paymentId, newStatus);
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
        ArgumentCaptor<Update> updateCaptor = ArgumentCaptor.forClass(Update.class);
        verify(mongoTemplate, times(1)).updateFirst(queryCaptor.capture(), updateCaptor.capture(), eq(PaymentEntity.class));
        Query query = queryCaptor.getValue();
        Update update = updateCaptor.getValue();

        assertTrue(query.getQueryObject().containsKey("id"));
        assertEquals(paymentId, query.getQueryObject().get("id"));
        assertEquals(newStatus.toString(), update.getUpdateObject().get("$set", org.bson.Document.class).get("status"));
        assertNotNull(update.getUpdateObject().get("$set", org.bson.Document.class).get("finishedAt"));
    }

    @Test
    void shouldFindByIdAndReturnPayment() {
        PaymentEntity entity = PaymentEntity.builder()
                .id("pay003")
                .orderId("order003")
                .paymentMethod("CREDIT")
                .amount(new BigDecimal("150.00"))
                .status("COMPLETED")
                .build();

        when(mongoTemplate.findById("pay003", PaymentEntity.class)).thenReturn(entity);

        Payment payment = paymentRepository.findById("pay003");

        assertNotNull(payment);
        assertEquals("pay003", payment.getId());
        assertEquals("order003", payment.getOrderId());
        assertEquals(PaymentMethod.CREDIT, payment.getMethod());
        assertEquals(new BigDecimal("150.00"), payment.getAmount());
        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
    }

    @Test
    void shouldReturnNullWhenNotFoundData() {
        when(mongoTemplate.findById("notfound", PaymentEntity.class)).thenReturn(null);

        Payment payment = paymentRepository.findById("notfound");

        assertNull(payment);
    }
}
