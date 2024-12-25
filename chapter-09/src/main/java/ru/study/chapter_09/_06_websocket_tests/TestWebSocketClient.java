/**
 * Интерфейс клиента WebSocket с точки зрения тестирования.
 */

public interface TestWebSocketClient {
    Flux<WebSocketMessage> sendAndReceive(Publisher<?> outgoingSource);
}