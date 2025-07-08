package video.microservice.infraestructure.adapters.services.mercadopago.downstream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import payment.microservice.domain.authorize.PaymentAuthorizationCreated;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MercadoPagoServiceTest {

    @InjectMocks
    private MercadoPagoService mercadoPagoService;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mercadoPagoService = new MercadoPagoService();
        mercadoPagoService = spy(mercadoPagoService);
        mercadoPagoService.setMpAccessToken("Bearer mock_token");
        mercadoPagoService.setMpInstoreUrl("https://mock-api.mercadopago.com");
        mercadoPagoService.setNotificationUrl("https://mock-notification.url");
        setWebClient(mercadoPagoService, webClient);
    }
    private void setWebClient(MercadoPagoService target, WebClient mock) {
        try {
            var field = MercadoPagoService.class.getDeclaredField("webClient");
            field.setAccessible(true);
            field.set(target, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void shouldAuthorizeSuccess() {
        Payment payment = Payment.builder().id("id").method(PaymentMethod.CREDIT).orderId("orderId").currency("USD")
                .amount(new BigDecimal("10.00")).status(PaymentStatus.PENDING).quantity(10).build();

        PixAuthorizeResponse mockResponse = new PixAuthorizeResponse();
        mockResponse.setQrCode("pix-fastfood-qrcode");
        mockResponse.setInStoreOrderId("orderId");

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(eq(PixAuthorizeResponse.class)))
                .thenReturn(Mono.just(mockResponse));

        PaymentAuthorizationCreated result = mercadoPagoService.authorize(payment);

        assertNotNull(result);
        assertTrue(result.isCreated());
        assertEquals("pix-fastfood-qrcode", result.getData());
        assertTrue(result.getMessage().contains("orderId"));
    }

    @Test
    void shouldFailAuthorizeWithNullResponse() {
        Payment payment = Payment.builder().id("id").method(PaymentMethod.CREDIT).orderId("orderId").currency("USD")
                .amount(new BigDecimal("10.00")).status(PaymentStatus.PENDING).quantity(10).build();

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(eq(PixAuthorizeResponse.class)))
                .thenReturn(Mono.empty());

        PaymentAuthorizationCreated result = mercadoPagoService.authorize(payment);

        assertNotNull(result);
        assertFalse(result.isCreated());
        assertNull(result.getData());
        assertEquals("Error calling Mercado Pago Gateway", result.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAuthorize() {
        Payment payment = Payment.builder().id("id").method(PaymentMethod.CREDIT).orderId("orderId").currency("USD")
                .amount(new BigDecimal("10.00")).status(PaymentStatus.PENDING).quantity(10).build();

        when(webClient.post()).thenThrow(new RuntimeException("Simulated error"));

        PaymentAuthorizationCreated result = mercadoPagoService.authorize(payment);

        assertNotNull(result);
        assertFalse(result.isCreated());
        assertEquals("Simulated error", result.getMessage());
        assertNull(result.getData());
    }


    @Test
    void shouldHaveErrorResponseOnAPIWhenAuthorize() {
        Payment payment = Payment.builder().id("id").method(PaymentMethod.CREDIT).orderId("orderId").currency("USD")
                .amount(new BigDecimal("10.00")).status(PaymentStatus.PENDING).quantity(10).build();

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        ClientResponse clientResponse = mock(ClientResponse.class);
        when(clientResponse.statusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        when(clientResponse.bodyToMono(String.class))
                .thenReturn(Mono.just("Internal Server Error"));

        ArgumentCaptor<Predicate<HttpStatusCode>> predicateCaptor = ArgumentCaptor.forClass(Predicate.class);
        ArgumentCaptor<Function<ClientResponse, Mono<? extends Throwable>>> handlerCaptor = ArgumentCaptor.forClass(Function.class);

        when(responseSpec.onStatus(predicateCaptor.capture(), handlerCaptor.capture()))
                .thenReturn(responseSpec);

        RuntimeException simulatedException = new RuntimeException("Mercado Pago error [500]: Internal Server Error");
        when(responseSpec.bodyToMono(PixAuthorizeResponse.class)).thenReturn(Mono.error(simulatedException));

        PaymentAuthorizationCreated result = mercadoPagoService.authorize(payment);

        assertNotNull(result);
        assertFalse(result.isCreated());
        assertNull(result.getData());
        assertTrue(result.getMessage().contains("Mercado Pago error [500]: Internal Server Error"));

        assertTrue(predicateCaptor.getValue().test(HttpStatus.INTERNAL_SERVER_ERROR));

        Mono<? extends Throwable> errorMono = handlerCaptor.getValue().apply(clientResponse);
        StepVerifier.create(errorMono)
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().contains("Internal Server Error"))
                .verify();
    }
}
