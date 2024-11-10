package ru.study.chapter_07._03__mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookSpringDataMongoRepository
        extends MongoRepository<Book, Integer> {

    // Поиск книг по именам авторов отсортированных по году издания в обратном порядке.
    Iterable<Book> findByAuthorsOrderByPublishingYearDesc(String... authors);

    // Поиск книг написанных несколькими авторами.
    @Query("{ 'authors.1': { $exists: true } }")
    Iterable<Book> booksWithFewAuthors();
}
