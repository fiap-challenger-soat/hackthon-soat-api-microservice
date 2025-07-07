package payment.microservice.cucumber.stepdefinitions;

import io.cucumber.java.en.*;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.*;
import payment.microservice.domain.capture.PaymentCapture;
import payment.microservice.infraestructure.adapters.services.mercadopago.upstream.webhooks.MercadoPagoWebhook;
import payment.microservice.infraestructure.adapters.rest.request.NotificationRequest;
import payment.microservice.infraestructure.adapters.rest.response.PaymentStatusResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PaymentStatusSteps {

    private NotificationRequest notificationRequest;
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
    private WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
    private WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);
    private final WebClient webClient = mock(WebClient.class);
    private final MercadoPagoWebhook webhook = new MercadoPagoWebhook(webClient);

    private PaymentCapture result;

    @Given("uma notificação com o tópico {string}")
    public void uma_notificacao_com_topico(String topic) {
        notificationRequest = new NotificationRequest();
        notificationRequest.setTopic(topic);
        notificationRequest.setResource("http://mocked-url.com");
        webhook.setMpAccessToken("Bearer test_token");
    }

    @And("a API do Mercado Pago retorna status {string}")
    public void api_mercado_pago_retorna_status(String orderStatus) {
        PaymentStatusResponse mockResponse = new PaymentStatusResponse();
        mockResponse.setExternalReference("abc123");
        mockResponse.setOrderStatus(orderStatus);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(PaymentStatusResponse.class)).thenReturn(Mono.just(mockResponse));
    }

    @And("a API do Mercado Pago retorna um erro {int}")
    public void api_mercado_pago_retorna_erro(Integer statusCode) {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        ClientResponse clientResponse = mock(ClientResponse.class);
        when(clientResponse.statusCode()).thenReturn(HttpStatusCode.valueOf(statusCode));
        when(clientResponse.bodyToMono(String.class)).thenReturn(Mono.just("Erro interno"));

        ArgumentCaptor<Predicate<HttpStatusCode>> predicateCaptor = ArgumentCaptor.forClass(Predicate.class);
        ArgumentCaptor<Function<ClientResponse, Mono<? extends Throwable>>> handlerCaptor = ArgumentCaptor.forClass(Function.class);
        when(responseSpec.onStatus(predicateCaptor.capture(), handlerCaptor.capture()))
                .thenReturn(responseSpec);

        RuntimeException simulatedException = new RuntimeException("Mercado Pago error [500]: Internal Server Error");


        when(responseSpec.bodyToMono(PaymentStatusResponse.class)).thenReturn(Mono.error(simulatedException));
    }

    @When("o metodo paymentStatus é executado")
    public void metodo_paymentStatus_executado() {
        result = webhook.paymentStatus(notificationRequest);
    }

    @Then("o status retornado deve ser {word}")
    public void status_deve_ser(String expectedStatus) {
        assertNotNull(result);
        assertEquals(PaymentStatus.valueOf(expectedStatus), result.getStatus());
    }

    @Then("o retorno deve ser nulo")
    public void retorno_deve_ser_nulo() {
        assertNull(result);
    }
}
