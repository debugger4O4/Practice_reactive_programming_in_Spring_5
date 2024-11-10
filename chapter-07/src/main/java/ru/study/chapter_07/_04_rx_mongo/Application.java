package ru.study.chapter_07._04_rx_mongo;


public class Application {

    @Autowired
    private ReactiveSpringDataMongoBookRepository rxBookRepository;
    ...
    Flux<Book> books = Flux.just(
            new Book("The Martian", 2011, "Andy Weir"),
            new Book("Blue Mars", 1996, "Kim Stanley Robinson")
    );

    rxBookRepository
            .saveAll(books)
            // then() - преобразует поток так, что он вернет только одно из двух событий: onComplete или onError.
            .then()
            // Запись в журнал сообщения после завершения работы потока.
            .doOnSuccess(ignore -> log.info("Books saved in DB"))
            .subscribe();

    // Вывод результатов, поставляемых в реактивном потоке.
    private void reportResults(String message, Flux<Book> books) {
        books.map(Book::toString)
                // Сбор строковых представлений всех книг в одно сообщение.
                .reduce(
                        new StringBuffer(),
                        (sb, b) -> sb.append(" - ")
                                .append(b)
                                .append("\n"))
                .doOnNext(sb -> log.info(message + "\n{}", sb))
                .subscribe();
    }

    // Прочитать список книг в БД.
    Flux<Book> allBooks = rxBookRepository.findAll();
    reportResults("All books in DB:", allBooks);

    // Отыскать все книги Энди Вейера.
    Flux<Book> andyWeirBooks = rxBookRepository
            .findByAuthorsOrderByPublishingYearDesc(Mono.just("Andy Weir"));
    reportResults("All books by Andy Weir:", andyWeirBooks);
}
