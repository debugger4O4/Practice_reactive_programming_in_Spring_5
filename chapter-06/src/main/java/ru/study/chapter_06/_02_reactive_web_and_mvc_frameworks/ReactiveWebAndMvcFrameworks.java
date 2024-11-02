package ru.study.chapter_06._02_reactive_web_and_mvc_frameworks;

/**
 * Реактивные фреймворки Web и MVC.
 */
public class ReactiveWebAndMvcFrameworks {
    // Реактивный интерфейс.
    interface HandlerMapping {
        // Старая реалзация.
        /* HandlerExecutionChain getHandler(HttpServletRequest request); */
        // Новая реализация.
        Mono<Object> getHandler(ServerWebExchange exchange);
    }
    // Реактивная версия.
    interface HandlerAdapter {
        boolean supports(Object handler);
    // Старая реалзация.
//        /* ModelAndView handle(
//                HttpServletRequest request, HttpServletResponse response,
//                Object handler); */
    // Новая реализация.
        // ServerWebExchange объединяет экземпляры запроса и ответа.
        Mono<HandlerResult> handle(
                ServerWebExchange exchange,
                Object handler
        );
    }
}
