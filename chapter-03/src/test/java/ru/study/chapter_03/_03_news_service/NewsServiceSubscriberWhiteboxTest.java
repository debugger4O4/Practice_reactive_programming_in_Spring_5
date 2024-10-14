package ru.study.chapter_03._03_news_service;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.reactivestreams.tck.SubscriberWhiteboxVerification;
import org.reactivestreams.tck.TestEnvironment;
import ru.study.chapter_03._03_news_service.dto.NewsLetter;

public class NewsServiceSubscriberWhiteboxTest
        extends SubscriberWhiteboxVerification<NewsLetter> {

    public NewsServiceSubscriberWhiteboxTest() {
        super(new TestEnvironment());
    }

    @Override
    public Subscriber<NewsLetter> createSubscriber(WhiteboxSubscriberProbe<NewsLetter> probe) {
        return new NewsServiceSubscriber(1) {
            public void onSubscribe(Subscription s) {
                super.onSubscribe(s);
                probe.registerOnSubscribe(new SubscriberPuppet() {
                    public void triggerRequest(long elements) {
                        s.request(elements);
                    }

                    public void signalCancel() {
                        s.cancel();
                    }
                });
            }

            public void onNext(NewsLetter newsLetter) {
                super.onNext(newsLetter);
                probe.registerOnNext(newsLetter);
            }

            public void onError(Throwable t) {
                super.onError(t);
                probe.registerOnError(t);
            }

            public void onComplete() {
                super.onComplete();
                probe.registerOnComplete();
            }
        };
    }

    @Override
    public NewsLetter createElement(int element) {
        return new StubNewsLetter(element);
    }
}
