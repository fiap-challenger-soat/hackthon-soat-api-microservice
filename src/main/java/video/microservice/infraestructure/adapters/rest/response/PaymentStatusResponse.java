package payment.microservice.infraestructure.adapters.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import payment.microservice.infraestructure.adapters.services.util.OrderItemDetails;

import java.util.List;

@Data
public class PaymentStatusResponse {
    private Long id;
    private String status;
    @JsonProperty("external_reference")
    private String externalReference;
    @JsonProperty("preference_id")
    private String preferenceId;
    private String marketplace;
    @JsonProperty("notification_url")
    private String notificationUrl;
    @JsonProperty("date_created")
    private String dateCreated;
    @JsonProperty("last_updated")
    private String lastUpdated;
    @JsonProperty("sponsor_id")
    private Long sponsorId;
    @JsonProperty("shipping_cost")
    private Double shippingCost;
    @JsonProperty("total_amount")
    private Double totalAmount;
    @JsonProperty("site_id")
    private String siteId;
    @JsonProperty("paid_amount")
    private Double paidAmount;
    @JsonProperty("refunded_amount")
    private Double refundedAmount;
    private List<OrderItemDetails> items;
    private Boolean cancelled;
    @JsonProperty("additional_info")
    private String additionalInfo;
    @JsonProperty("application_id")
    private Long applicationId;
    @JsonProperty("is_test")
    private Boolean isTest;
    @JsonProperty("order_status")
    private String orderStatus;
    @JsonProperty("client_id")
    private String clientId;
}
