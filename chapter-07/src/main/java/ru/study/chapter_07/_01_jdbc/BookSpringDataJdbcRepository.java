package ru.study.chapter_07._01_jdbc;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Repository
public interface BookSpringDataJdbcRepository
        extends CrudRepository<Book, Integer> {

    @Query("SELECT * FROM book WHERE LENGTH(title) = " +
            "(SELECT MAX(LENGTH(title)) FROM book)")
    List<Book> findByLongestTitle();

    @Query("SELECT * FROM book WHERE LENGTH(title) = " +
            "(SELECT MIN(LENGTH(title)) FROM book)")
    Stream<Book> findByShortestTitle();

    /*
     Асинхронная выполнение. Поток выполнения клиента не будет заблокирован в ожидании результатов. Главный поток все
     еще будет блокироваться.
     */
    @Async
    @Query("SELECT * FROM book b " +
            "WHERE b.title = :title")
    CompletableFuture<Book> findBookByTitleAsync(
            @Param("title") String title);

    /*
    Поток клиента не должен блокироваться, пока не появятся первые результаты, после чего появится возможность пакетной
    обработки результатов. Для первой стадии соответствующий поток выполнения должен быть заблокирован, а потом блокируется
    и поток выполнения клиента для извлечения следующей порции данных.
     */
    @Async
    @Query("SELECT * FROM book b " +
            "WHERE b.id > :fromId AND b.id < :toId")
    CompletableFuture<Stream<Book>> findBooksByIdBetweenAsync(
            @Param("fromId") Integer from,
            @Param("toId") Integer to);
}
