package ru.study.chapter_07._08_r2dbc;

/**
 * Реактивное соединение с реляционной БД.
 */
public class PostgresConfiguration {

    // Настройка фабрики соединений с БД.
    PostgresqlConnectionFactory pgConnectionFactory =
            PostgresqlConnectionConfiguration.builder()
                    .host("host")
                    .database("database")
                    .username("username")
                    .password("password")
                    .build();

    // Получение реактивного API.
    R2dbc r2dbc = new R2dbc(pgConnectionFactory);

    // inTransaction - создание транзакции, handle - обертка реактивного соединения и предоставление удобного API.
    r2dbc.inTransaction(handle ->
        handle
                // Выполнение инструкции SQL и возврат количества затронутых записей.
            .execute("insert into book (id, title, publishing_year) " +
            "values ($1, $2, $3)",
            20, "The Sands of Mars", 1951)
            // Вывод количества обновленных данных.
        .doOnNext(inserted -> log.info("{} rows was inserted into DB", inserted))
            // Запуск другой транзакции, чтобы выбрать названия всех книг.
    ).thenMany(r2dbc.inTransaction(handle ->
        handle.select("SELECT title FROM book")
            // Отображение отдельных записей.
        .mapResult(result ->
            result.map((row, rowMetadata) ->
            // Извлечение поля "title".
                row.get("title", String.class)))))
            /*
             Реактивная регистрация всех сигналов onNext. Каждый сигнал сопровождается названием книги, включая
             добавленное на шаге.
             */
    .subscribe(elem -> log.info(" - Title: {}", elem));
}
