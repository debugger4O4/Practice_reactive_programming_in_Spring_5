package ru.study.chapter_06._11_webflux_in_action;

/**
 * WebFlux в действии.
 */
public class WebFluxInAction {

    // Интерфейс - связь с удаленным сервером.
    public interface ChatService<T> {
        // Получение бесконечного потока сообщений.
        Flux<T> getMessagesStream();
        // Получение списка сообщений с конкретным идентификатором.
        Mono<List<T>> getMessagesAfter(String messageId);
    }

    // Реализация ChatService - подключение к потоковому Gitter API для приема новых сообщений.
    public class GitterService implements ChatService {
        Mono<List<T>> getMessagesAfter(String messageId) {
            ...
            return webClient
                  .get()
                    // Параметры удаленного сервера Gitter.
                  .uri(...)
                    // Получение и преобразование ответа в поток Mono<List<MessageResponse>>.
                  .retrieve()
                  .bodyToMono(
                      new ParametrizedTypeReference <List<MessageResponse>>() {}
                   )
                    // Определение тайм-аута для устойчивой связи со службой.
                   .timeout(Duration.ofSeconds(1))
                    // Повторная попытка в случае ошибки.
                   .retryBackoff(Long.MAX_VALUE, Duration.ofMillis(500));
        }

        // Подключение к конечной потоковой точке JSON.
        public Flux<MessageResponse> getMessagesStream() {
            return webClient
                    .get()
                    .uri(...)
                    .retrieve()
                    // Flux вместо Mono.
                    .bodyToFlux(MessageResponse.class)
                    .retryBackoff(Long.MAX_VALUE, Duration.ofMillis(500));
        }
    }

    // Обработка пользовательских запросов и ответ потоком сообщений.
    @RestController
    @RequestMapping("/api/v1/info")
    public class InfoResource {
        // Кеширование предопределнного числа элементов и пересылка последних элементов всем новым подписчика.
        private final ReplayProcessor<MessageVM> messagesStream  = ReplayProcessor.create(50);

        public InfoResource(ChatService<MessageResponse> chatService) {
            // Определение конвейера обработки, который объединяет поток последних сообщенией от службы Gitter.
            Flux.mergeSequential(
                chartService.getMessagesAfter(null)
                        .flatMapIterable(Function.identity())
                // Принятие сообщений в режиме реального времени.
                chatService.getMessagesStream();
            )
            // Отображение всех сообщений в модели представления.
            .map(...)
            // Подписка на поток.
            .subscribe(messagesStream);
        }

        // Метод-обработчки. Вызывается для каждого ноаого соединения к заданной точке.
        @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
        public Flux<MessageResponse> stream() {
            return messagesStream;
        }
}
