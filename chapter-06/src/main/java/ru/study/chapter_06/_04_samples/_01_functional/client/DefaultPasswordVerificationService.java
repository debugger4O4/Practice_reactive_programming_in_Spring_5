package ru.study.chapter_06._04_samples._01_functional.client;

/*
    Пример использования WebClient вместо устаревшего RestTemplate.
    webClient.create("http://localhost/api")
        Получение экземпляра построителя запроса.
        .get()
        Относительный путь.
        .uri("/users/{id}", userId)
        Составление запроса.
        .retrieve()
        Преобразование входящих данных.
        .bodyToMono(User.class)
        .map(...)
        .subscribe();
 */

//import reactor.core.publisher.Mono;
//
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;
//
//public class DefaultPasswordVerificationService
//        implements PasswordVerificationService {
//
//    final WebClient webClient;
//
//    public DefaultPasswordVerificationService(WebClient.Builder webClientBuilder) {
//        webClient = webClientBuilder
//                .baseUrl("http://localhost:8080")
//                .build();
//    }
//
//    @Override
//    public Mono<Void> check(String raw, String encoded) {
//        return webClient
//                .post()
//                .uri("/password")
                // Вставка тела запроса.
//                .body(BodyInserters.fromPublisher(
//                        Mono.just(new PasswordDTO(raw, encoded)),
//                        PasswordDTO.class
//                ))
                // Возвращение Mono, чтобы можно было использовать flatMap.
//                .exchange()
                // Если успех, то Mono.empty, иначе EXPECTATION_FAILED.
//                .flatMap(response -> {
//                    if (response.statusCode().is2xxSuccessful()) {
//                        return Mono.empty();
//                    }
//                    else if (response.statusCode() == EXPECTATION_FAILED) {
//                        return Mono.error(new BadCredentialsException("Invalid credentials"));
//                    }
//                    return Mono.error(new IllegalStateException());
//                });
//    }
//}
