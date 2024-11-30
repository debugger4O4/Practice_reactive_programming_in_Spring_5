package ru.study.chapter_07._10_rx_sync;

/**
 * Завернули блокирующее хранилище BookJpaRepository в реактивное RxBookRepository.
 */
public class App {

    Scheduler scheduler = Schedulers.newParallel("JPA", 10);
    BookJpaRepository jpaRepository = getBlockingRepository(...);

    RxBookRepository rxRepository =
            new RxBookRepository(jpaRepository, scheduler);

    Flux<Book> books = rxRepository
            .findByIdBetween(Mono.just(17), Mono.just(22));

    books
            .subscribe(b -> log.info("Book: {}", b));
}
