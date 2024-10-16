package ru.study.chapter_03._03_news_service;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.reactivestreams.tck.SubscriberWhiteboxVerification;
import org.reactivestreams.tck.TestEnvironment;
import ru.study.chapter_03._03_news_service.dto.NewsLetter;

/**
 * SubscriberWhiteboxVerification требует расширение Subscriber и реализацию дополнительных обработчиков. Охватывает
 * более широкий круг правил, регламинтирующих правильное поведение подписчика Subscriber, но не подходит для случаев,
 * когда создается финальный класс, который нельзя наследовать
 */
public class NewsServiceSubscriberWhiteboxTest
        extends SubscriberWhiteboxVerification<NewsLetter> {

    public NewsServiceSubscriberWhiteboxTest() {
        super(new TestEnvironment());
    }

    /*
    WhiteboxSubscriberProbe предоставляет механизм, который встраивается для упарвления запросом и прехвата входящих
    сигналов
     */
    @Override
    public Subscriber<NewsLetter> createSubscriber(WhiteboxSubscriberProbe<NewsLetter> probe) {
        return new NewsServiceSubscriber(1) {
            public void onSubscribe(Subscription s) {
                super.onSubscribe(s);
                // SubscriberPuppet - поддерживает прямой доступ к полученной подписке
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
