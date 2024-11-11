package ru.study.chapter_07._04_rx_mongo;

/**
 * Использование ReactiveMongoTemplate.
 */
public class RxMongoTemplateQueryService {

    private final ReactiveMongoOperations mongoOperations;
    // Конструктор...

    public Flux<Book> findBooksByTitle(String titleRegExp) {
        Query query = Query.query(new Criteria("title")
                // Принимает регулярные выражения, как критерий поиска.
                        .regex(titleRegExp))
                .limit(100);
        return mongoOperations.find(query, Book.class, "book");
    }
}
