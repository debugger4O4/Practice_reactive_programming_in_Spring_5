package ru.study.chapter_07._10_rx_sync;

public class RxBookRepository extends
        ReactiveCrudRepositoryAdapter<Book, Integer, BookJpaRepository> {

    public RxBookRepository(
            BookJpaRepository delegate,
            Scheduler scheduler
    ) {
        super(delegate, scheduler);
    }

    public Flux<Book> findByIdBetween(
            Publisher<Integer> lowerPublisher,
            Publisher<Integer> upperPublisher
    ) {
        // Получает два реактивных потока данных и подписывается на них с помощью zip.
        return Mono.zip(
                        Mono.from(lowerPublisher),
                        Mono.from(upperPublisher)
                ).flatMapMany(
                        /*
                         Когда оба значения получены, вызывается delegate и выполнение блокирующего кода передается
                         выделенному планировщику scheduler.
                         */
                        function((lower, upper) ->
                                Flux
                                        .fromIterable(delegate.findByIdBetween(lower, upper))
                                        .subscribeOn(scheduler)
                        ))
                // Передача разрешения потоков lower и upper, чтобы цикл обработки событий не тратил ресурсы.
                .subscribeOn(scheduler);
    }

    // Вызывает соответствующий метод под управлением выделенного scheduler и отображает Iterable в Flux.
    public Flux<Book> findShortestTitle() {
        return Mono.fromCallable(delegate::findShortestTitle)
                .subscribeOn(scheduler)
                .flatMapMany(Flux::fromIterable);
    }
}
