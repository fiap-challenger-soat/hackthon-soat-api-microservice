package video.microservice.infraestructure.adapters.services.mercadopago.downstream.request;

import org.junit.jupiter.api.Test;
import payment.microservice.infraestructure.adapters.services.util.OrderItemDetails;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PixAuthorizeRequestTest {

    @Test
    void shouldBuildPixAuthorizeRequestCorrectly() {
        String externalReference = "ORDER123";
        String title = "Payment Title";
        String description = "Payment for Order 123";
        String notificationUrl = "https://webhook.fast-food/notify";
        BigDecimal totalAmount = new BigDecimal("99.90");
        OrderItemDetails item = OrderItemDetails.builder()
                .unitMeasure("ML")
                .description("description")
                .quantity(2)
                .skuNumber("123")
                .title("title")
                .totalAmount(BigDecimal.TEN)
                .unitPrice(BigDecimal.ONE)
                .build();
        List<OrderItemDetails> items = List.of(item);

        // Act
        PixAuthorizeRequest request = PixAuthorizeRequest.builder()
                .externalReference(externalReference)
                .title(title)
                .description(description)
                .notificationUrl(notificationUrl)
                .totalAmount(totalAmount)
                .items(items)
                .build();

        // Assert
        assertThat(request.getExternalReference()).isEqualTo(externalReference);
        assertThat(request.getTitle()).isEqualTo(title);
        assertThat(request.getDescription()).isEqualTo(description);
        assertThat(request.getNotificationUrl()).isEqualTo(notificationUrl);
        assertThat(request.getTotalAmount()).isEqualByComparingTo(totalAmount);
        assertThat(request.getItems()).hasSize(1);
        assertThat(request.getItems().get(0).getDescription()).isEqualTo("description");
    }

    @Test
    void shouldAllowSettingFieldsViaAllConstructor() {
        PixAuthorizeRequest request = new PixAuthorizeRequest("REF123", "title",
                "description", "https://webhook.fast-food/notify",  BigDecimal.TEN, new ArrayList<>());


        assertThat(request.getExternalReference()).isEqualTo("REF123");
        assertThat(request.getTitle()).isEqualTo("title");
        assertThat(request.getDescription()).isEqualTo("description");
        assertThat(request.getNotificationUrl()).isEqualTo("https://webhook.fast-food/notify");
        assertThat(request.getTotalAmount()).isEqualByComparingTo(BigDecimal.TEN);
    }
}
