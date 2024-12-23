/**
 * Продвинутые приемы тестирования с использованием StepVerifier.
 */

public class AdvancedTestingTechniques {

    /*
     Аннулирование подписки после удовлетворения некоторых ожиданий.
     Отключение или отписка от WebSocket после получения сообщений Connected и Price: $12.00.
     */
    Flux<String> websocketPublisher = ...
    StepVerifier
            .create(websocketPublisher)
            .expectSubscription()
            .expectNext("Connected")
            .expectNext("Price: $12.00")
            .thenCancel()
            .verify();

    // Проверка, работает ли система в соответствии с выбранной стратегией обратного давления.
    Flux<String> websocketPublisher = ...
    Class<Exception> expectedErrorClass =
            reactor.core.Exceptions.failWithOverflow().getClass();

    StepVerifier
            .create(websocketPublisher.onBackpressureBuffer(5), 0)
            .expectedSubscription()
            .thenRequest(1)
            .expectNext("Connected")
            .thenRequest(1)
            .expectNext("Price: $12.00")
            .expectError(expectedErrorClass)
            .verify();

    // Генерация новых событий в ходе тестирования.
    TestPublisher<String> idsPublisher = TestPublisher.create();

    StepVerifier
            .create(walletsRepository.findAllById(isPublisher))
            .expectSupscription()
            .then(() -> isPublisher.next("1"))
            .assertNext(w -> assertThat(w, hasProperty("id", equalTo("1"))))
            .then(() -> idsPublisher.next("2"))
            .assertNext(w -> assertThat(w, hasProperty("id", equalTo("2"))))
            .then(idsPublisher::complete)
            .expectComplete()
            .verify();
}