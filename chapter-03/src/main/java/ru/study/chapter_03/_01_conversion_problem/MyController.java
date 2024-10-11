package ru.study.chapter_03._01_conversion_problem;

import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MyController {
    private final List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();

    {
        this.messageConverters.add(new ByteArrayHttpMessageConverter());
        this.messageConverters.add(new StringHttpMessageConverter());
        this.messageConverters.add(new MappingJackson2HttpMessageConverter());
    }

    // Deprecated
    /*
     Обработка запросов, действует асинхронно и возвращает ListenableFuture для обработки результата выполнения
     неблокирующим способом
     */
//    @RequestMapping(produces = MediaType.TEXT_PLAIN_VALUE)
//    public ListenableFuture<?> requestData() {
//        AsyncRestTemplate httpClient = new AsyncRestTemplate();
//        AsyncDatabaseClient databaseClient = new FakeAsyncDatabaseClient();

//        Преобразование AsyncRestTemplate в CompletionStage
//        CompletionStage<String> completionStage = AsyncAdapters.toCompletion(httpClient.execute(
//                "http://localhost:8080/hello",
//                HttpMethod.GET,
//                null,
//                new HttpMessageConverterExtractor<>(String.class, messageConverters)
//        ));
//
//        Преобразование в ListenableFuture
//        return AsyncAdapters.toListenable(databaseClient.store(completionStage));
//    }

    @GetMapping("/hello")
    public Mono<String> requestData() {
        WebClient httpClient = WebClient.create();
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080").build().toUri();
        return httpClient.get()
                .uri(uri)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(String.class))
                .onErrorResume(e -> Mono.empty());
    }
}
