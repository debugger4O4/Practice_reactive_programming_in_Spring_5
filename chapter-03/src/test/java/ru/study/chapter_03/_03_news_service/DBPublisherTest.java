package ru.study.chapter_03._03_news_service;
/*
Проблемы с зависимостями
 */
//import com.mongodb.reactivestreams.client.MongoCollection;
//import io.reactivex.rxjava3.annotations.NonNull;
//import io.reactivex.rxjava3.core.Flowable;
//import org.reactivestreams.Publisher;
//import org.reactivestreams.tck.PublisherVerification;
//import org.reactivestreams.tck.TestEnvironment;
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.BeforeClass;
//import ru.study.chapter_03.WithEmbeddedMongo;
//import ru.study.chapter_03._03_news_service.dto.News;
//
//import java.io.IOException;
//import java.util.concurrent.TimeUnit;

public class DBPublisherTest {
//        extends PublisherVerification<News> implements
//        WithEmbeddedMongo {
//
//    private final MongoCollection<News> collection;
//
//    public DBPublisherTest() {
//        super(new TestEnvironment(2000, 2000));
//        this.collection = mongoClient().getDatabase("news")
//                .getCollection("news", News.class);
//    }
//
//    @BeforeClass
//    static void up() throws IOException {
//        WithEmbeddedMongo.setUpMongo();
//    }
//
//    @AfterClass
//    static void down() {
//        WithEmbeddedMongo.tearDownMongo();
//    }
//
//    @Override
//    public Publisher<News> createPublisher(long elements) {
//        prepareRandomData(elements);
//        return new DBPublisher(collection,"tech");
//    }
//
//    @Override
//    public Publisher<News> createFailedPublisher() {
//        return null;
//    }
//
//    private void prepareRandomData(long elements) {
//        if (elements <= 0) {
//            return;
//        }
//
//        @NonNull Flowable<Object> successFlowable = Flowable.fromPublisher(collection.drop())
//                .ignoreElements()
//                .andThen(
//                        Flowable
//                                .rangeLong(0L, elements)
//                                .map(l -> NewsHarness.generate())
//                                .buffer(500, TimeUnit.MILLISECONDS)
//                                .flatMap(collection::insertMany)
//                );
//
//        if (elements == Long.MAX_VALUE || elements == Integer.MAX_VALUE) {
//            successFlowable.subscribe();
//        } else {
//            successFlowable.blockingSubscribe();
//        }
//    }
}
