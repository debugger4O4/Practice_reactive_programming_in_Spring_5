package ru.study.chapter_03._03_news_service;

import org.reactivestreams.Subscriber;
import org.reactivestreams.tck.SubscriberBlackboxVerification;
import org.reactivestreams.tck.TestEnvironment;
import ru.study.chapter_03._03_news_service.dto.NewsLetter;

public class NewsServiceSubscriberTest
        extends SubscriberBlackboxVerification<NewsLetter> {

    public NewsServiceSubscriberTest() {
        super(new TestEnvironment());
    }

    @Override
    public Subscriber<NewsLetter> createSubscriber() {
        return new NewsServiceSubscriber(Integer.MAX_VALUE);
    }

    @Override
    public NewsLetter createElement(int element) {
        return new StubNewsLetter(element);
    }

    @Override
    public void triggerRequest(Subscriber<? super NewsLetter> s) {
        ((NewsServiceSubscriber) s).eventuallyReadDigest();
    }
}
