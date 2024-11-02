package ru.study.chapter_06._04_samples._02_websocket;

import reactor.core.publisher.Mono;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
/**
 * Серверный WebSocket API.
 */
public class EchoWebSocketHandler implements WebSocketHandler {

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session
                // Создание конвеера обработки входящих сообщений.
                .receive()
                // Преобразование полезной нагрузи из байтов в текст.
                .map(WebSocketMessage::getPayloadAsText)
                // Текст.
                .map(tm -> "Echo: " + tm)
                // Заворачивание нового сообщения в текст.
                .map(session::textMessage)
                // Посылка сообщения клиенту. send() принимает Publisher<WebSocketMessage> и возвращает Mono<Void>.
                .as(session::send);
    }
}