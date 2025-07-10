Feature: Consultar status de pagamento no Mercado Pago

  Scenario: Pagamento aprovado via webhook
    Given uma notificação com o tópico "merchant_order"
    And a API do Mercado Pago retorna status "payment_completed"
    When o metodo paymentStatus é executado
    Then o status retornado deve ser APPROVED

  Scenario: Pagamento falhou via webhook
    Given uma notificação com o tópico "merchant_order"
    And a API do Mercado Pago retorna um erro 500
    When o metodo paymentStatus é executado
    Then o retorno deve ser nulo
