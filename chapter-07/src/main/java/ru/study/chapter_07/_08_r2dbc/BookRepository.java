package ru.study.chapter_07._08_r2dbc;

/**
 * Использование R2DBC вместе с Spring Data R2DBC.
 */
public interface BookRepository
        extends ReactiveCrudRepository<Book, Integer> {

    @Query("SELECT * FROM book WHERE publishing_year = " +
            "(SELECT MAX(publishing_year) FROM book)")
    Flux<Book> findTheLatestBooks();
}
