package ru.study.chapter_01._06_spring_futures;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.study.chapter_01._02_commons.ExamplesCollection;

/**
 * Spring 4 MVC долгое время не поддерживал CompletionStage и для тех же целей педлагал собственную реализацию
 * в форме ListenableFuture. Причина - разработчики Spring 4 стремились сохранить совместимость со старыми версиям Java
 * Пример грязной асинхронной связи
 */
@RestController
@RequestMapping("api/v2/resource/a")
public class AsyncServiceOne {
    private static final String PORT = "8080";

    @GetMapping
    public void process() {
        /*
         Deprecated
        public Future<?> process() {
            AsyncRestTemplate template = new AsyncRestTemplate();
            SuccessCallback onSuccess = r -> System.out.println("Success");
            FailureCallback onFailure = e -> System.out.println("Failure");
            ListenableFuture<?> response = template.get(
                    "http://localhost:" + PORT + "/api/v2/resource/b",
                    ExamplesCollection.class
            );

            response.addCallback(onSuccess, onFailure);

            return response;
         */

        WebClient template = WebClient.create("http://localhost:" + PORT);
        Mono<ExamplesCollection> response = template.get()
                .uri("/api/v2/resource/b")
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(ExamplesCollection.class));

        response.subscribe(exampleCollection -> {
            System.out.println("Success");
        }, error -> {
            System.out.println("Failure");
        });
    }
}
