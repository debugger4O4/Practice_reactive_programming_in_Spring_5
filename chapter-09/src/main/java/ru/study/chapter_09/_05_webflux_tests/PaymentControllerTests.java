/**
 * Пример запуска нормального полного интеграционного тестирования.
 */

@ImportAutoCofiguration({
        TestSecurityConfiguration.class,
        TestWebClientBuilderConfiguration.class
})
@RunWith(SpringRunner.class)
@WebFluxTest
@AutoConfigureWebTestClient
public class PaymentControllerTests {

    @Autowired
    WebTestClient client;

    @MockBean
    ExchangeFunction exchangeFunction;

    @Test
    @WithMockUser
    public void verifyPaymentsWasSentAndStored() {
        Mockito
                .when(exchangeFunction.exchange(Mockito.any()))
                .thenReturn(
                        Mono.just(MockClientResponse.create(201, Mono.empty())));
        client.post()
                .uri("/payments/")
                .syncBody(new Payment())
                .exchage()
                .expectStatus().is2xxSuccessful()
                .returnResult(String.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .expectComplete()
                .verify();

        Mockito.verify(exchangeFunction).exchange(Mockito.any());
    }
}