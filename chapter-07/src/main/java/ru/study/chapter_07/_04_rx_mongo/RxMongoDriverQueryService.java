package ru.study.chapter_07._04_rx_mongo;

/**
 * Использование реактивных драйверов (MongoDB).
 */
public class RxMongoDriverQueryService {

    private final MongoClient mongoClient;

    // Использование реактивного драйвера.
    public Flux<Book> findBooksByTitleRegex(String regex) {
        return Flux.defer(() -> {
                    // Bson формат хранения данных в MongoDB.
                    Bson query = Filters
                            .regex(titleRegex);

                    if (negate) {
                        query = Filters.not(query);
                    }
                    return mongoClient
                            .getDatabase("test-database")
                            .getCollection("book")
                            .find(query);
                })
                .map(doc -> new Book(
                        doc.getObjectId("id"),
                        doc.getString("title"),
                        doc.getInteger("pubYear"),
                        // ... другие процедуры отображения.
                ));
    }
}
