package payment.microservice.infraestructure.adapters.services.mercadopago.upstream.webhooks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import payment.microservice.domain.capture.PaymentCapture;
import payment.microservice.infraestructure.adapters.rest.request.NotificationRequest;
import payment.microservice.infraestructure.adapters.rest.response.PaymentStatusResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MercadoPagoWebhookTest {

    @InjectMocks
    private MercadoPagoWebhook webhook;

    @Mock
    private WebClient mockWebClient;

    @Mock
    private WebClient.RequestHeadersUriSpec uriSpec;

    @Mock
    private WebClient.RequestHeadersSpec headersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private NotificationRequest notificationRequest;

    private final String accessToken = "Bearer test-token";

    @BeforeEach
    void setup() {
        webhook.setMpAccessToken(accessToken);
        setWebClient(webhook, mockWebClient);
    }

    private void setWebClient(MercadoPagoWebhook target, WebClient mock) {
        try {
            var field = MercadoPagoWebhook.class.getDeclaredField("webClient");
            field.setAccessible(true);
            field.set(target, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldApprovedPaymentStatus() {
        when(notificationRequest.getTopic()).thenReturn("merchant_order");
        when(notificationRequest.getResource()).thenReturn("https://fast-food.com");

        PaymentStatusResponse fakeResponse = new PaymentStatusResponse();
        fakeResponse.setOrderStatus("payment_completed");
        fakeResponse.setExternalReference("123");

        when(mockWebClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri("https://fast-food.com")).thenReturn(uriSpec);
        when(uriSpec.header("Authorization", accessToken)).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(PaymentStatusResponse.class)).thenReturn(Mono.just(fakeResponse));

        PaymentCapture capture = webhook.paymentStatus(notificationRequest);

        assertNotNull(capture);
        assertEquals("123", capture.getPaymentId());
        assertEquals(PaymentStatus.APPROVED, capture.getStatus());
    }

    @Test
    void shouldFailedPaymentStatus() {
        when(notificationRequest.getTopic()).thenReturn("merchant_order");
        when(notificationRequest.getResource()).thenReturn("https://fast-food.com");

        PaymentStatusResponse fakeResponse = new PaymentStatusResponse();
        fakeResponse.setOrderStatus("rejected");
        fakeResponse.setExternalReference("456");

        when(mockWebClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri("https://fast-food.com")).thenReturn(uriSpec);
        when(uriSpec.header("Authorization", accessToken)).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(PaymentStatusResponse.class)).thenReturn(Mono.just(fakeResponse));

        PaymentCapture capture = webhook.paymentStatus(notificationRequest);

        assertNotNull(capture);
        assertEquals("456", capture.getPaymentId());
        assertEquals(PaymentStatus.FAILED, capture.getStatus());
    }

    @Test
    void shouldHaveNullResponse() {
        when(notificationRequest.getTopic()).thenReturn("merchant_order");
        when(notificationRequest.getResource()).thenReturn("https://fast-food.com");

        when(mockWebClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri("https://fast-food.com")).thenReturn(uriSpec);
        when(uriSpec.header("Authorization", accessToken)).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(PaymentStatusResponse.class)).thenReturn(Mono.empty());

        PaymentCapture capture = webhook.paymentStatus(notificationRequest);
        assertNull(capture);
    }

    @Test
    void shouldHaveInvalidTopic() {
        when(notificationRequest.getTopic()).thenReturn("payment");

        PaymentCapture capture = webhook.paymentStatus(notificationRequest);
        assertNull(capture);
    }

    @Test
    void shouldReturnTimeout() {
        when(notificationRequest.getTopic()).thenReturn("merchant_order");
        when(notificationRequest.getResource()).thenReturn("https://fast-food.com");

        when(mockWebClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(uriSpec);
        when(uriSpec.header(anyString(), anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(PaymentStatusResponse.class)).thenThrow(new RuntimeException("timeout"));

        PaymentCapture capture = webhook.paymentStatus(notificationRequest);
        assertNull(capture);
    }

    @Test
    void shouldHandleErrorResponseFromAPI() {
        NotificationRequest request = new NotificationRequest();
        request.setTopic("merchant_order");
        request.setResource("https://fast-food.com");

        when(mockWebClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(request.getResource())).thenReturn(headersSpec);
        when(headersSpec.header(anyString(), anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);

        ClientResponse mockResponse = mock(ClientResponse.class);
        lenient().when(mockResponse.statusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        when(mockResponse.bodyToMono(String.class)).thenReturn(Mono.just("Erro no servidor"));

        ArgumentCaptor<Predicate<HttpStatusCode>> predicateCaptor = ArgumentCaptor.forClass(Predicate.class);
        ArgumentCaptor<Function<ClientResponse, Mono<? extends Throwable>>> handlerCaptor = ArgumentCaptor.forClass(Function.class);

        when(responseSpec.onStatus(predicateCaptor.capture(), handlerCaptor.capture())).thenReturn(responseSpec);
        RuntimeException simulatedException = new RuntimeException("Mercado Pago error [500]: Internal Server Error");
        when(responseSpec.bodyToMono(PaymentStatusResponse.class)).thenReturn(Mono.error(simulatedException));

        PaymentCapture result = webhook.paymentStatus(request);

        assertNull(result, "Deveria retornar null em caso de erro");

        assertTrue(predicateCaptor.getValue().test(HttpStatus.INTERNAL_SERVER_ERROR));

        Mono<? extends Throwable> errorMono = handlerCaptor.getValue().apply(mockResponse);
        StepVerifier.create(errorMono)
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().contains("Failed to get payment status:"))
                .verify();
    }
}
