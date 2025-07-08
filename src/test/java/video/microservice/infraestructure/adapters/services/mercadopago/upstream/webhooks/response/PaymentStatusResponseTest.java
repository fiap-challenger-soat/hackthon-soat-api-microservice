package video.microservice.infraestructure.adapters.services.mercadopago.upstream.webhooks.response;

import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;

public class PaymentStatusResponseTest {
    public void shouldCreatePaymentStatusResponse(){
         PaymentStatusResponse paymentStatusResponse = new PaymentStatusResponse();
         paymentStatusResponse.setId(123L);
         paymentStatusResponse.setStatus("status");
         paymentStatusResponse.setExternalReference("externalReference");
        paymentStatusResponse.setPreferenceId("preferenceId");
        paymentStatusResponse.setMarketplace("marketplace");
        paymentStatusResponse.setNotificationUrl("notificationUrl");
        paymentStatusResponse.setDateCreated("dateCreated");
        paymentStatusResponse.setLastUpdated("lastUpdated");
        paymentStatusResponse.setSponsorId(12L);
        paymentStatusResponse.setShippingCost(12.02);
        paymentStatusResponse.setTotalAmount(21.11);
        paymentStatusResponse.setSiteId("siteId");
        paymentStatusResponse.setPaidAmount(5.44);
        paymentStatusResponse.setRefundedAmount(8.66);
        paymentStatusResponse.setItems(new ArrayList<>());
        paymentStatusResponse.setCancelled(Boolean.FALSE);
        paymentStatusResponse.setAdditionalInfo("AddInfo");
        paymentStatusResponse.setApplicationId(10L);
        paymentStatusResponse.setIsTest(Boolean.TRUE);
        paymentStatusResponse.setOrderStatus("orderStatus");
        paymentStatusResponse.setClientId("clientId");



        Assertions.assertNotNull(paymentStatusResponse.getIsTest());
        Assertions.assertNotNull(paymentStatusResponse.getApplicationId());
        Assertions.assertNotNull(paymentStatusResponse.getItems());
        Assertions.assertNotNull(paymentStatusResponse.getRefundedAmount());
        Assertions.assertNotNull(paymentStatusResponse.getPaidAmount());
        Assertions.assertNotNull(paymentStatusResponse.getTotalAmount());
        Assertions.assertNotNull(paymentStatusResponse.getShippingCost());
        Assertions.assertNotNull(paymentStatusResponse.getSponsorId());
        Assertions.assertNotNull(paymentStatusResponse.getId());
        Assertions.assertEquals("status", paymentStatusResponse.getStatus());
        Assertions.assertEquals("externalReference", paymentStatusResponse.getExternalReference());
        Assertions.assertEquals("preferenceId", paymentStatusResponse.getPreferenceId());
        Assertions.assertEquals("marketplace", paymentStatusResponse.getMarketplace());
        Assertions.assertEquals("notificationUrl", paymentStatusResponse.getNotificationUrl());
        Assertions.assertEquals("dateCreated", paymentStatusResponse.getDateCreated());
        Assertions.assertEquals("lastUpdated", paymentStatusResponse.getLastUpdated());
        Assertions.assertEquals("siteId", paymentStatusResponse.getSiteId());
        Assertions.assertEquals("AddInfo", paymentStatusResponse.getAdditionalInfo());
        Assertions.assertEquals("orderStatus", paymentStatusResponse.getOrderStatus());
        Assertions.assertEquals("clienteId", paymentStatusResponse.getClientId());
    }
}
