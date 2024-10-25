package ru.study.chapter_05;

/**
 * Простое реактивное приложение, имитирующее датчик IoT.
 * <br>
 * Генерирует данные IoT, сохраняет данные в базе данных (MongoDB) и сообщает о последних измерениях через SSE.
 * Все операции являются реактивными (SSE, запись MongoDB, чтение MongoDB).
 * <br>
 * Использование встроенной MongoDB.
 * <br>
 * Входная точка: http://localhost:8080
 */
//@Slf4j
//@SpringBootApplication
//@RequiredArgsConstructor
//public class Chapter5ReactiveApplication {
//
//    public static void main(String[] args) {
//        SpringApplication.run(Chapter5ReactiveApplication.class, args);
//    }
//
//    @Bean
//    public RouterFunction<ServerResponse> routerFunction(
//            SensorReadingRepository sensorReadingRepository
//    ) {
//        return RouterFunctions
//                .route(
//                        GET("/"),
//                        serverRequest -> ServerResponse
//                                .ok()
//                                .contentType(MediaType.APPLICATION_STREAM_JSON)
//                                .body(sensorReadingRepository.findBy(), SensorsReadings.class));
//    }
//
//}