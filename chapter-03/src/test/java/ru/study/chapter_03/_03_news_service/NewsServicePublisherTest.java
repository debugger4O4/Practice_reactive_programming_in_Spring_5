package ru.study.chapter_03._03_news_service;

/*
Проблема с зависимостями в WithEmbeddedMongo
*/
//
//import com.mongodb.client.MongoCollection;
//import com.mongodb.reactivestreams.client.Success;
//import io.reactivex.rxjava3.core.Flowable;
//import org.reactivestreams.Publisher;
//import org.reactivestreams.tck.PublisherVerification;
//import org.reactivestreams.tck.TestEnvironment;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.BeforeMethod;
//import ru.study.chapter_03.WithEmbeddedMongo;
//import ru.study.chapter_03._03_news_service.dto.News;
//import ru.study.chapter_03._03_news_service.dto.NewsLetter;
//
//import java.util.concurrent.TimeUnit;

//public class NewsServicePublisherTest extends PublisherVerification<NewsLetter>
//        implements WithEmbeddedMongo {
//
//    public NewsServicePublisherTest() {
//        super(new TestEnvironment(2000, 2000));
//    }
//
//    @Override
//    public Publisher<NewsLetter> createPublisher(long elements) {
//        MongoCollection<News> collection = mongoClient().getDatabase("news")
//                .getCollection("news", News.class);
//        int period = elements > 0 ? (int)(1000 / elements) : 1;
//        prepareItemsInDatabase(elements);
//
//        Publisher<NewsLetter> newsServicePublisher = new NewsServicePublisher(smp ->
//                new ScheduledPublisher<>(
//                        () -> new NewsPreparationOperator(
//                                new DBPublisher(
//                                        collection,
//                                        "tech"
//                                ),
//                                "Some Digest"
//                        ),
//                        period == 0 ? 1 : period, TimeUnit.MILLISECONDS
//                ).subscribe(smp)
//        );
//
//        newsServicePublisher = Flowable.fromPublisher(newsServicePublisher)
//                .take(elements);
//
//        return newsServicePublisher;
//    }
//
//    @Override
//    public Publisher<NewsLetter> createFailedPublisher() {
//        MongoCollection<News> collection = mongoClient().getDatabase("news")
//                .getCollection("news",
//                        News.class);
//        WithEmbeddedMongo.tearDownMongo();
//        return new NewsServicePublisher(smp ->
//                new ScheduledPublisher<>(
//                        () -> new NewsPreparationOperator(
//                                new DBPublisher(
//                                        collection,
//                                        "tech"
//                                ),
//                                "Some Digest"
//                        ),
//                        1, TimeUnit.MILLISECONDS
//                ).subscribe(smp)
//        );
//    }
//
//    @BeforeMethod
//    static void up() throws IOException {
//        WithEmbeddedMongo.setUpMongo();
//    }
//
//    @AfterMethod
//    static void down() {
//        WithEmbeddedMongo.tearDownMongo();
//    }
//
//    private void prepareItemsInDatabase(long elements) {
//        if (elements <= 0) {
//            return;
//        }
//
//        MongoCollection<News> collection = mongoClient().getDatabase("news")
//                .getCollection("news", News.class);
//
//        Flowable<Success> successFlowable = Flowable.fromPublisher(collection.drop())
//                .ignoreElements()
//                .andThen(Flowable.rangeLong(0L,
//                                elements)
//                        .map(l -> NewsHarness.generate())
//                        .buffer(500,
//                                TimeUnit.MILLISECONDS)
//                        .flatMap(collection::insertMany));
//
//        if (elements == Long.MAX_VALUE || elements == Integer.MAX_VALUE) {
//            successFlowable.subscribe();
//        }
//        else {
//            successFlowable.blockingSubscribe();
//        }
//    }
//}
