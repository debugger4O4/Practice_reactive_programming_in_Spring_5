package ru.study.chapter_03._03_news_service;

import org.reactivestreams.Subscriber;
import org.reactivestreams.tck.SubscriberBlackboxVerification;
import org.reactivestreams.tck.TestEnvironment;
import ru.study.chapter_03._03_news_service.dto.NewsLetter;

/**
 * Проверка подписчика Subscriber
 * SubscriberBlackboxVerification - комплект, который используется, когда реализация Subscriber взята извне и нет
 * никакого законного способа расширить ее поведение
 */
public class NewsServiceSubscriberTest
        extends SubscriberBlackboxVerification<NewsLetter> {

    // Создание конкретного экземпляра
    public NewsServiceSubscriberTest() {
        super(new TestEnvironment());
    }

    @Override
    public Subscriber<NewsLetter> createSubscriber() {
        return new NewsServiceSubscriber(Integer.MAX_VALUE);
    }

    // Фабрика новых элементов, генерирует новые экземпляры NewsLetter
    @Override
    public NewsLetter createElement(int element) {
        return new StubNewsLetter(element);
    }

    /*
    SubscriberBlackboxVerification предполагает недоступность внутренних механизмов подписчика, а значит недоступность
    экземпляра подписки Subscription внутри Subscriber, т.е. каким-то образом нужно вручную запустить процесс, используя
    данный API
     */
    @Override
    public void triggerRequest(Subscriber<? super NewsLetter> s) {
        ((NewsServiceSubscriber) s).eventuallyReadDigest();
    }
}
