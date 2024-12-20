/**
 * RSocket в Java.
 * Двусторонняя связь между клиентом и сервером.
 */

public class RsocketJava {

    // Определение сервера RSocket.
    RSocketFactory
            .receive() // Создание сервера.
            .acceptor(new SocketAcceptorImpl()) // Определение метода-обработчика для входящих соединений с клиентами.
            .transport(TcpServerTransport.create("localhost", 7000)) // Определение предпочтительного транспорта.
            .start()
            .subscribe();

    // Определение клиента RSocket.
    Rsocket socket = RSocketFactory
            .connect() // Возвращает клиентский экземпляр RSocket.
            .transport(TcpClientTransport.create("localhost", 7000))
            .start()
            .block(); // Используется, чтобы дождаться успешной установки соединений и получить активный экземпляр RSocket.

    // Выполнение запроса к серверу.
    socket
            // Модель канала. Отправка и получение потока.
            .requestChannel(
                    Flux.interval(Duration.ofMillis(1000))
                      .map(i -> DefaultPayload.create("Hello [" + i + "]")) /*
                                                                                Заворачивание сообщения в класс Payload,
                                                                                потому что сообщение в потоке по умолчанию
                                                                                является классом Payload.
                                                                            */
            )
            .map(Payload::getDataUtf8)
            .doFinallly(signalType -> socket.dispose())
            .then()
            .block();
}