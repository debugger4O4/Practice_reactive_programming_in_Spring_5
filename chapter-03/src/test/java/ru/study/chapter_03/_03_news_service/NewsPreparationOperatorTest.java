package ru.study.chapter_03._03_news_service;

import io.reactivex.rxjava3.core.Flowable;
import org.reactivestreams.Publisher;
import org.reactivestreams.tck.PublisherVerification;
import org.reactivestreams.tck.TestEnvironment;
import ru.study.chapter_03._03_news_service.dto.News;
import ru.study.chapter_03._03_news_service.dto.NewsLetter;

public class NewsPreparationOperatorTest extends PublisherVerification<NewsLetter> {

    public NewsPreparationOperatorTest() {
        super(new TestEnvironment());
    }

    @Override
    public Publisher<NewsLetter> createPublisher(long elements) {
        return new NewsPreparationOperator(
                Flowable.just(new News()),
                "test"
        );
    }

    @Override
    public Publisher<NewsLetter> createFailedPublisher() {
        return new NewsPreparationOperator(
                Flowable.error(new RuntimeException()),
                "test"
        );
    }

    @Override
    public long maxElementsFromPublisher() {
        return 1;
    }
}
