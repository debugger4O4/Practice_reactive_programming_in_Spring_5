package ru.study.chapter_06._04_samples._02_websocket;

import org.springframework.stereotype.Controller;

/**
 * Сравнение WebFlux WebSocket и Spring WebSocket.
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
