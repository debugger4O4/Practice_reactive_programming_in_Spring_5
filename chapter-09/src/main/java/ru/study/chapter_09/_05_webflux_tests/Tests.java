public class Tests {

    // Проверка получения списка пользователей.
    @Test
    public void verifyRespondWithExpectedPayments() {
        PaymentService paymentService = Mockito.mock(PaymentService.class);
        PaymentController controller = new PaymentController(paymentService);

        prepareMockResponse(paymentService);
        WebTestClients
                .bindToController(controller)
                .build()
                .get()
                .uri("/payments/")
                .exchange()
                .expectHeader().contentTypeCompatibleWith(APPLICATION_JSON)
                .expectStatus().is2xxSuccessful()
                .returnResult(Payment.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .expectNextCount(5)
                .expectComplete()
                .verify();
    }
}