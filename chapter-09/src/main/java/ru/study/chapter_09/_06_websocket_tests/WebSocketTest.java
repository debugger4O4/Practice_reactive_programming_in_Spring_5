/**
 * Тестирование WebSocket.
 */

public class WebSocketTest {

    // Подключение к целевому серверу и проверка полученных данных.
    new ReactorNettyWebSocketClient()
        .execute(uri, new WebSocketHandler() { ... })

    // Адаптация WebSocketClient к TestWebSocketClient.
    Mono.create(sink ->
            sink.onCancel(
                    client.execute(uri, session -> {
                        sink.success(session);
                        return Mono.never();
                    })
                    .doOnError(sink::error)
                    .subscribe();
            )
    );

    // Адаптация WebSocketSession. Послыка входящих сообщений WebSocketMessage.
    public Flux<WebSocketMessage> sendAndReceive(
            Publisher<?> outgoingSource
    ) {
        ...
        .flatMapMany(session ->
                session.receive()
                        .mergeWith(
                                Flux.from(outgoingSource)
                                   .map(Object::toString)
                                   .map(session::textMessage)
                                   .as(session::send)
                                   .then(Mono.empty())
                        )
                );
    }
}