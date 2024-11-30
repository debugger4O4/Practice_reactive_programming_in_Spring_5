package ru.study.chapter_07._10_rx_sync;

@Repository
public interface BookJpaRepository
        extends CrudRepository<Book, Integer> {

    Iterable<Book> findByIdBetween(int lower, int upper);

    @Query("SELECT b FROM Book b WHERE " +
            "LENGTH(b.title) = (SELECT MIN(LENGTH(b2.title)) FROM Book b2)")
    Iterable<Book> findShortestTitle();
}
