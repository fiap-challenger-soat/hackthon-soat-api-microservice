package payment.microservice.infraestructure.adapters.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ErrorHandlerTest {
    private ErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        errorHandler = new ErrorHandler();
    }

    @Test
    void shouldThrowBusinessException() {
        PaymentError error = PaymentError.builder()
                .code(400)
                .level("ERROR")
                .message("Bad request")
                .description("Invalid payment data")
                .build();

        PaymentException paymentException = new PaymentException(HttpStatus.BAD_REQUEST, List.of(error));

        ResponseEntity<?> response = errorHandler.handlerBusinessException(paymentException);
        Object body = response.getBody();
        List<?> errors = (List<?>) body;

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(body);
        assertInstanceOf(List.class, body);
        assertEquals(1, errors.size());
        assertEquals(error, errors.get(0));
    }

    @Test
    void shouldThrowGenericException() {
        RuntimeException ex = new RuntimeException("Generic exception");
        ResponseEntity<?> response = errorHandler.handlerGenericException(ex);


        Object body = response.getBody();
        List<?> errors = (List<?>) body;
        PaymentError error = (PaymentError) errors.get(0);


        assertEquals(1, errors.size());
        assertNotNull(body);
        assertInstanceOf(List.class, body);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), error.getCode());
        assertEquals("ERROR", error.getLevel());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), error.getMessage());
        assertEquals("Generic exception", error.getDescription());
    }

}
