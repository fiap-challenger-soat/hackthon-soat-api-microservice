package video.microservice.infraestructure.adapters.services.mercadopago.downstream.response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import payment.microservice.infraestructure.adapters.services.mercadopago.downstream.request.PixAuthorizeRequest;

import java.math.BigDecimal;
import java.util.ArrayList;

public class PixAuthorizeResponseTest {

    @Test
    public void shouldCreatePixAuthorizeResponseWithBuilder(){
        PixAuthorizeRequest pixAuthorizeRequest = PixAuthorizeRequest.builder().title("title").totalAmount(BigDecimal.TEN).
                    items(new ArrayList<>())
                .description("description").externalReference("extReference").build();

        Assertions.assertEquals("title", pixAuthorizeRequest.getTitle());
        Assertions.assertEquals(BigDecimal.TEN, pixAuthorizeRequest.getTotalAmount());
        Assertions.assertNotNull(pixAuthorizeRequest.getItems());
        Assertions.assertEquals("description", pixAuthorizeRequest.getDescription());
        Assertions.assertEquals("extReference", pixAuthorizeRequest.getExternalReference());
    }

    @Test
    public void shouldCreatePixAuthorizeResponseWithConstructor(){
        PixAuthorizeRequest pixAuthorizeRequest = new PixAuthorizeRequest("extReference","title",
                "description",
                "notificationUrl",
                BigDecimal.TEN,
                new ArrayList<>());

        Assertions.assertEquals("title", pixAuthorizeRequest.getTitle());
        Assertions.assertEquals(BigDecimal.TEN, pixAuthorizeRequest.getTotalAmount());
        Assertions.assertNotNull(pixAuthorizeRequest.getItems());
        Assertions.assertEquals("description", pixAuthorizeRequest.getDescription());
        Assertions.assertEquals("extReference", pixAuthorizeRequest.getExternalReference());
    }


}
