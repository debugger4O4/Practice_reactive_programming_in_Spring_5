package ru.study.chapter_07._09_rxjava2_jdbc;

public class RxBookRepository {

    // Библиотека не генерирует запросы автоматически. Определяем нужный запрос.
    private static final String SELECT_BY_YEAR_BETWEEN =
            "select * from book where " +
                    "publishing_year >= :from and publishing_year <= :to";

    // Инициализация БД.
    private final String url = "jdbc:h2:mem:db";
    private final int poolSize = 25;
    private final Database database = Database.from(url, poolSize);

    //
    public Flowable<Book> findByYearBetween(
            Single<Integer> from,
            Single<Integer> to
    ) {
        return Single
                // Подписка на поток, возвращающий аргументы запроса.
                .zip(from, to, Tuple2::new)
                .flatMapPublisher(tuple -> database
                        // Вызов select.
                        .select(SELECT_BY_YEAR_BETWEEN)
                        // Заполнение параметров запроса.
                        .parameter("from", tuple._1())
                        .parameter("to", tuple._2())
                        // Преобразование записи JDBC в сущность Book. Возвращает Flowable<Book> == Flux<Book>.
                        .autoMap(Book.class));
    }
}
