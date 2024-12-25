/**
 * Проверка общей функциональности.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment =
    SpringBootTest.WebEnvironment.DEFINED_PORT)
public class WebSocketAPITests {

    @Test
    @WithMockUser
    public void checkThatUserIsAbleToMakeATrade() {
        URI uri = URI.ctreate("ws://localhost:8080/stream");
        TestWebSocketClient client = TestWebSocketClient.create(uri);
        TestPublisher<String> testPublisher = TestPublisher.create();
        Flux<String> inbound - testPublisher
                .flux()
                .subscribeWith(ReplayProcessor.create(1))
                .transform(client::sendAndReceive)
                .map(WebSocketMessage::getPayloadAsText);

        StepVerifier
                .create(inbound)
                .expectSubscription()
                .then(() -> testPublisher.next("TRADES|BTC"))
                .expectNext("PRICE|AMOUNT|CURRENCY")
                .then(() -> testPublisher.next("TRADE: 10123|1.54|BTC"))
                .expectNext("10123|1.54|BTC")
                .then(() -> testPublisher.next("TRADE: 10090|-0.01|BTC"))
                .expectNext("10090|-0.01|BTC")
                .thenCanceL()
                .verify();
    }
}