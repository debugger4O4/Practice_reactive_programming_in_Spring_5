package ru.study.chapter_06._04_samples._02_websocket;

import org.springframework.stereotype.Controller;

/**
 * Сравнение WebFlux WebSocket и Spring WebSocket.
 * <br>
 * Разница между WebFlux WebSocket и Spring WebSocket заключается в некоторых особенностях:
 * Spring WebSocket блокирует взаимодействие с IO, в то время как Spring WebFlux предлагает полностью неблокирующую
 * запись и чтение.
 * Модуль WebFlux обеспечивает лучшую потоковую абстракцию, используя спецификацию Reactive Streams и проект Reactor.
 * В то время как интерфейс WebSocketHandler из старого модуля WebSocket позволяет обрабатывать только одно сообщение
 * за раз.
 * Кроме того, Spring WebFlux WebSocket построен на основе проекта Reactor Netty, который является высокопроизводительной
 * платформой для сетевых приложений
 */
@Controller
public class GreetingController {

    // Реализация Spring WebSocket. @MessageMapping - объявление конченой точки.
//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
//    public Greeting greeting(HelloMessage message) {
//        return new Greeting("Hello, " + message.getName() + "!");
//    }

}
