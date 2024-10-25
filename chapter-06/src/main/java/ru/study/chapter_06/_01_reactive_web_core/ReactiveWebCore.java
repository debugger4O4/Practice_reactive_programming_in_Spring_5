package ru.study.chapter_06._01_reactive_web_core;

/**
 * Реактивное веб-ядро.
 */
public class ReactiveWebCore {

    // Макет интерфейса, представляющего входящее сообщение.
//    interface ServerHttpRequest {
//        ...
        // Открытие доступа к входящим байтам.
//        Flux<DataBuffer> getBody();
//        ...
//    }
    // Макет интерфейса ответа, сопутствующего интерфейсу erverHttpRequest.
//    interface ServerHttpResponse {
//        ...
        // Принимает любой класс Publisher<? extends DataBuffer>.
//        Mono<Void> writeWith(Publisher<? extends DataBuffer> body);
//        ...
//    }
    // Контейнер для экземпляров запросов и ответов HTTP.
//    interface ServerWebExchange {
//        ...
//        ServerHttpRequest getRequest();
//        ServerHttpResponse getResponse();
//        ...
        // Информация о веб-сеансе, восстановленном из входящего запроса.
//        Mono<WebSession> getSession();
//        ...
//    }
    /*
     Центральная точка входа в любые HTTP-взаимодействия. Абстрактный класс DispatcherServlet, что позволяет определить
     на его основе любую релизацию.
     */
//    interface WebHandler {
//        Mono<Void> handle(ServerWebExchange exchange);
//    }
    // Объединяет в цепочку несколько экземпляров WebFilter.
//    interface WebFilterChain {
//        Mono<Void> filter(ServerWebExchange exchange);
//    }
    // Реактивное представление класса Filter.
//    interface WebFilter {
//        Mono<Void> filter(ServerWebExchange exch, WebFilterChain chain);
//    }
    /*
     Контракт самого низкого уровня до реактивной обработки HTTP-запросов. Отвечает за прямый взаимодействия с
     ServerHttpRequest и ServerHttpResponse. Завершение иерархии абстракции.
     */
//    public interface HttpHandler {
//        Mono<Void> handle(
//                ServerHttpRequest request,
//                ServerHttpResponse response
//        );
//    }
}
