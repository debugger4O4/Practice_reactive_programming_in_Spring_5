package ru.study.chapter_06._04_samples._02_websocket;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerAdapter;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
/**
 * Настройка WebSocket API.
 */
@Configuration
public class WebSocketConfiguration {

    @Bean
    public HandlerMapping handlerMapping() {
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        // Нстройка отображения путей.
        mapping.setUrlMap(Collections.singletonMap("/ws/echo", new EchoWebSocketHandler()));
        /*
         Чтобы SimpleUrlHandlerMapping обрабатывал запросы раньше других экземпляров HandlerMapping, он должен иметь
         самый высокий приоритет.
         */
        mapping.setOrder(-1);
        return mapping;
    }

    // Обновляет HTTP-соединение до соединения WebSocket и затем вызывает WebSocketHandler.handle().
    @Bean
    public HandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
