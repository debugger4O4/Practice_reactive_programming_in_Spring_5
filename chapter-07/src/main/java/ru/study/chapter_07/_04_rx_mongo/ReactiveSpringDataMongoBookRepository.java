package ru.study.chapter_07._04_rx_mongo;

import org.bson.types.ObjectId;
import org.reactivestreams.Publisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReactiveSpringDataMongoBookRepository
        extends ReactiveMongoRepository<Book, ObjectId> {

    // @Meta - для дополнительных настроек.
    @Meta(maxScanDocuments = 3)
    Flux<Book> findByAuthorsOrderByPublishingYearDesc(Publisher<String> authors);

    @Query("{ 'authors.1': { $exists: true } }")
    Flux<Book> booksWithFewAuthors();
}
