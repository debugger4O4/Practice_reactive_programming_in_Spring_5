package ru.study.chapter_06._04_samples.server;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import reactor.netty.DisposableServer;
//import reactor.netty.http.server.HttpServer;
//import ru.study.chapter_06._04_samples.client.PasswordDTO;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.HttpHandler;
//import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.reactive.function.server.RouterFunction;
//import org.springframework.web.reactive.function.server.RouterFunctions;
//import org.springframework.web.reactive.function.server.ServerResponse;
//
//import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
//import static org.springframework.web.reactive.function.server.RouterFunctions.route;
//
//public class StandaloneApplication {
//    static Logger LOGGER = LoggerFactory.getLogger(StandaloneApplication.class);
//
//    public static void main(String... args) {
//        long start = System.currentTimeMillis();
//        HttpHandler httpHandler = RouterFunctions.toHttpHandler(routes(
//                new BCryptPasswordEncoder(18)
//        ));
//        ReactorHttpHandlerAdapter reactorHttpHandler = new ReactorHttpHandlerAdapter(httpHandler);
        // Настройка сервера.
//        DisposableServer server = HttpServer.create()
//                .host("localhost")
//                .port(8080)
//                .handle(reactorHttpHandler)
                // Запуск серверного движка.
//                .bindNow();
//
//        LOGGER.debug("Started in " + (System.currentTimeMillis() - start) + " ms");
        // Удаление созданного сервера, чтобы предовратить преднамеренное завершение приложения.
//        server.onDispose()
//                .block();
//    }
//
//    public static RouterFunction<ServerResponse> routes(PasswordEncoder passwordEncoder) {
//        return
//                route(POST("/password"),
//                        request -> request
                                // Отображение входящего запроса.
//                                .bodyToMono(PasswordDTO.class)
                                // Проверка текстовой строки с закодированным паролем.
//                                .map(p -> passwordEncoder.matches(p.getRaw(), p.getSecured()))
                                // Пароль совпал - ОК, иначе EXPECTATION_FAILED.
//                                .flatMap(isMatched -> isMatched
//                                        ? ServerResponse.ok()
//                                        .build()
//                                        : ServerResponse.status(HttpStatus.EXPECTATION_FAILED)
//                                        .build()
//                                )
//                );
//    }
//}
