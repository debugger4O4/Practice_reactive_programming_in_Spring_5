package ru.study.chapter_07._05_cassandra;

/**
 * Использование асинхронных драйверов (Cassandra).
 */
public class DefaultBridgeReactiveSession {

    /*
    Mono<ReactiveResultSet> обертывает асинхронный com.datastax.driver.core.ResultSet, поддерживающий разбиение на
    страницы. Первая страница с результатом извлекается, когда возвращается экземпляр ResultSet, следующие - только после
    потребления результатов с первой старницы. ReactiveResultSet адаптирует это поведение методом со следующей сигнатурой:
    Flux<Row> rows().
     */
    public Mono<ReactiveResultSet> execute(Statement statement) {
        // creaate() - создание нового экземпляра Mono, который откладывает операции до появления подписчика.
        return Mono.create(sink -> {
            try {
                // Асинхронное выполнение запроса.
                ListenableFuture<ResultSet> future = this.session
                        .executeAsync(statement);
                ListenableFuture<ReactiveResultSet> resultSetFuture =
                        // Заворачивание асинхронного ResultSet в реактивный аналог ReactiveResultSet.
                        Futures.transform(
                                future, DefaultReactiveResultSet::new
                        );
                // adaptFuture() - отображение Future в Mono.
                adaptFuture(resultSetFuture, sink);
            } catch (Exception cause) {
                sink.error(cause);
            }
        });
    }
}
