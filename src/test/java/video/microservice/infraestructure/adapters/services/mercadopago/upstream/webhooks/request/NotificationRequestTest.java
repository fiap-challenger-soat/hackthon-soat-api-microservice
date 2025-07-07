package payment.microservice.infraestructure.adapters.services.mercadopago.upstream.webhooks.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import payment.microservice.infraestructure.adapters.rest.request.NotificationRequest;

public class NotificationRequestTest {
    @Test
    public void shouldCreateNotificationRequestNoArgsConstructor(){
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setResource("resource");
        notificationRequest.setTopic("topic");

        Assertions.assertEquals("resource", notificationRequest.getResource());
        Assertions.assertEquals("topic", notificationRequest.getTopic());
    }
}
