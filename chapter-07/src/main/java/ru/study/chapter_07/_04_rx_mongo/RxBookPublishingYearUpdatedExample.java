package ru.study.chapter_07._04_rx_mongo;

/**
 * Объединение операций с хранилищем.
 */
public class RxBookPublishingYearUpdatedExample {

    // Бизнем-метод.
    public Mono<Book> updatedBookYearByTitle(
            Mono<String> title,
            Mono<Integer> newPublishingYear
    )

    // Тест, проверяющий работу updatedBookYearByTitle().
    Instant start = now();
    // Имитация записи с задержкой в 1 секунду.
    Mono<String> title = Mono.delay(Duration.ofSeconds(1))
            .thenReturn("Artemis")
            .doOnSubscribe(s -> log.info("Subscribed for title"))
            // Выводит запись в журнал сразу после получения названия.
            .doOnNext(t -> log.info("Book title resolved: {}" , t));

    // Имитация записи с задержкой в 2 секунды.
    Mono<Integer> publishingYear = Mono.delay(Duration.ofSeconds(2))
            .thenReturn(2017)
            .doOnSubscribe(s -> log.info("Subscribed for publishing year"))
            // Выводит запись в журнал сразу после получения названия.
            .doOnNext(t -> log.info("New publishing year resolved: {}" , t));

    // Бизнем-метод.
    updatedBookYearByTitle(title, publishingYear)
        // Выводит запись в журнал после получения подтверждения.
         .doOnNext(b -> log.info("Publishing year updated for the book: {}", b))
            .hasElement()
         .doOnSuccess(status -> log.info("Updated finished {}, took: {}",
    // Вывод записи об успехе или неудаче операции.
    status ? "successfully" : "unsuccessfully",
    between(start, now())))
            .subscribe();

    /* Первая итерация */
    private Mono<Book> updatedBookYearByTitle(
            Mono<String> title,
            Mono<Integer> newPublishingYear
    ) {
        // Обращение к хранилищу.
        return rxBookRepository.findOneByTitle(title)
                // Поиск нужной сущности.
                .flatMap(book -> newPublishingYear
                        // Подписка на получение нового года.
                        .flatMap(year -> {
                            // Изменение сущности Book.
                            book.setPublishYear(year);
                            return rxBookRepository.save(book);
                        }));
    }

    /* Вторая итерация */
    private Mono<Book> updatedBookYearByTitle(
            Mono<String> title,
            Mono<Integer> newPublishingYear
    ) {
        // Объединение двух значений и подписка на них.
        return Mono.zip(title, newPublishingYear)
                // Получение контейнера Tuple2 после получения обоих значений.
                .flatMap((Tuple2<String, Integer> data) -> {
                    // Распкаовка значения.
                    String titleVal = data.getT1();
                    // Распкаовка значения.
                    Integer yearVal = data.getT2();
                    // Запрос сущности и, после получения сущности, меняем ее год и сохраняем в БД.
                    return rxBookRepository
                            .findOneByTitle(Mono.just(titleVal))
                            .flatMap(book -> {
                                book.setPublishingYear(yearVal);
                                return rxBookRepository.save(book);
                            });
                });
    }

    /* Третья итерация */
    private Mono<Book> updatedBookYearByTitle(
            Mono<String> title,
            Mono<Integer> newPublishingYear
    ) {
        return Mono.zip(title, newPublishingYear)
                /*
                 Замена извлечения элементов из объекта Tuple2 вызовом метода function() класса TupleUtils. Продолжение
                 работы с уже извлеченными данными.
                 */
                .flatMap(function((titleValue, yearValue) ->
                        rxBookRepository
                                // Заворачивание titleValue в Mono.
                                .findOneByTitle(Mono.just(titleValue))
                                .flatMap(book -> {
                                    book.setPublishingYear(yearValue);
                                    return rxBookRepository.save(book);
                                })));
    }

    /* Четвертая итерация */
    private Mono<Book> updatedBookYearByTitle(
            Mono<String> title,
            Mono<Integer> newPublishingYear
    ) {
        // Одновременная подписка на получение названия и года издания.
        return Mono.zip(
                newPublishingYear,
                rxBookRepository.findOneByTitle(title)
                // Изменение года издания после получения и посылка запроса на сохранение.
        ).flatMap(function((yearValue, bookValue) -> {
            bookValue.setPublishingYear(yearValue);
            return rxBookRepository.save(bookValue);
        }));
    }
}
