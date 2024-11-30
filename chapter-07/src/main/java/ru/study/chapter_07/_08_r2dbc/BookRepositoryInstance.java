package ru.study.chapter_07._08_r2dbc;

public class BookRepositoryInstance {

    BookRepository customerRepository2(PostgresqlConnectionFactory factory) {
        // TransactionalDatabaseClient - базовая поддержка транзакций.
        TransactionalDatabaseClient txClient =
                TransactionalDatabaseClient.builder()
                        .connectionFactory(factory)
                        .build();
        // RelationalMappingContext - контекст, чтобы отображать записи в сущности и обратно.
        RelationalMappingContext context = new RelationalMappingContext();
        // Создание соответствующей фабрики хранилищ. R2dbcRepositoryFactory знает, как создать ReactiveCrudRepository.
        return new R2dbcRepositoryFactory(txClient, context)
                // Генерация BookRepository.
                .getRepository(BookRepository.class);
    }

    // Реактивный конвейер.
    bookRepository.findTheLatestBooks()
        .doOnNext(book -> log.info("Book: {}", book))
        .count()
        .doOnSuccess(count -> log.info("DB contains {} latest books", count))
        .block();

}
