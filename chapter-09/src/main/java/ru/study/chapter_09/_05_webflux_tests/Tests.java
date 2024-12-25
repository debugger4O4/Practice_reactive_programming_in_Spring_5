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

    /*
     Проверка некоторого предположения относительно веб-сайта http://www.bbc.com и терпение неудачи из-за разницы между
     ожидаемым и фактическим ответом.
     */
    WebTestClient webTestClient = WebTestClient
            .bindToServer()
            .baseUrl("http://www.bbc.com")
            .build();

    webTestClient
            .get()
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectHeader().exists("ETag")
            .expectBody().json("{}")
}