/**
 * Виртуальное время.
 */

public class VirtualTime {

    // Наивный способ публикации событий с определеннным интервалом.
    public Flux<String> sendWithInterval() {
        return Flux.interval(Duration.ofMinutes(1))
                .zipWith(Flux.just("a", "b", "c"))
                .map(Tuple2::getT2);
    }

    // Проверка для sendWithInterval().
    StepVerifier
            .create(sendWithInterval())
            .expectSubdcription()
            .expectNext("a", "b", "c")
            .expectComplete()
            .verify();

    // Сокращение времени выполнения тестирования.
    StepVerifier
            .create(sendWithInterval())
            .expectSubdcription()
        // Замена реального времени виртуальным.
            .then(() -> VirtualTimeScheduler
                .get()
                .advancedTimeBY(Duration.ofMinutes(3))
            )
            .expectNext("a", "b", "c")
            .expectComplete()
            .verify();

    // Ограничение времени, затрачиваемое на тестирование.
    Duration took = StepVerifier
            .withVirtualTime(() -> sendWithInterval())
            .expectSubscription()
            .thenAwait(Duration.ofMinutes(3)) // Применение ограничения времени.
            .expectNext("a", "b", "c")
            .expectComplete()
            .verify();

    System.out.println("Verification took: " + took);

    // Проверка, что во время тестирования не было никаких дргих событий.
    StepVerifier
            .withVirtualTime(() -> sendWithInterval())
            .expectSubscription();
            .expectNoEvent(Duration.ofMinutes(1)) // Проверяет, не было ли событий.
            .expectNext("a")
            .expectNoEvent(Duration.ofMinutes(2))
            .expectNext("b")
            .expectNoEvent(Duration.ofMinutes(3))
            .expectNext("c")
            .expectComplete()
            .verify();

    /*
     Пререгруженный thenAwait() без аргументов - запуск любых задач, которые еще не выполнены и которе планируется
     выполнить в текущее вирутальное время или до него.
     */
    StepVerifier
            .withVirtualTime(() ->
                Flux.virtual(Duration.ofMillis(0), Duration.ofMillis(1000))
                    .zipWith(Flux.just("a", "b", "c"))
                    .map(Tuple2::getT2)
            )
            .expectSubscription()
            .thenAwait() // Без thenAwait() тест зависнет.
            .expectNext("a")
            .expectNoEvent(Duration.ofMillis(1000))
            .expectNext("b")
            .expectNoEvent(Duration.ofMillis(1000))
            .expectNext("c")
            .expectNoEvent(Duration.ofMillis(1000))
            .expectComplete()
            .verify();
}