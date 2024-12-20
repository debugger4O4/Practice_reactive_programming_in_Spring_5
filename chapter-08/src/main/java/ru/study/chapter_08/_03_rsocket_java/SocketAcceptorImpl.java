/**
 * Класс для работы с входящими соединениями с клиентами.
 * Реализация SocketAcceptor.
 */

public class SocketAcceptorImpl implements SocketAcceptor {

    @Override
    public Mono<RSocket> accept(
            ConnectionSetupPayload setupPayload, // Первое рукопожатие со стороны клиента во время подкючения.
            RSocket reactiveSocket
    ) {
        return Mono.just(new AbstractRSocket() { /*
                                                    Объявление обработчика RSocket.
                                                    AbstractRSocket() - абстрактная реализация интерфейса RSocket.
                                                 */
                @Override
                public Flux<Payload> requestChannel(
                        Publisher<Payload> payloads
                ) {
                    return Flux.from(payloads)
                            .map(Payload::getDataUtf8)
                            .map(s -> "Echo: " + s)
                            .map(DefaultPayload::create);
                }
           }
        )
    }
}