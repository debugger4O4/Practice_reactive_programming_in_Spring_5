/**
 * RSocket в Spring Framework.
 */

@SpringBootApplication
@EnableReactiveSocket
public static class TestApplication {

    @RequestManyMapping(
            value = "/stream1",
            mimeType = "application/json"
    )
    public Flux<String> stream1(@Payload String a) {
        return Flux.just(a)
                .mergeWith(
                        Flux.interval(Duration.ofMillis(100))
                                .map(i -> "1. Stream Message: [" + i + "]")
                )
    }

    @RequestManyMapping(
            value = "/stream2",
            mimeType = "application/json"
    )
    public Flux<String> stream2(@Payload String b) {
        return Flux.just(b)
                .mergeWith(
                        Flux.interval(Duration.ofMillis(100))
                                .map(i -> "2. Stream Message: [" + i + "]")
                )
    }

    // Создание клиента.
    ReactiveSocketClient client = new ReactiveSocketClient(rSocket);
    TestClient clientProxy = client.create(TestClient.class);

    Flux.merge(
            clientProxy.receiveStream1("a"),
            clientProxy.receiveStream1("b")
    )
    .log()
    .subscribe()
}