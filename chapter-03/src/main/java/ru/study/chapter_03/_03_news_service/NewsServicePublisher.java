package ru.study.chapter_03._03_news_service;

import org.reactivestreams.Publisher;
import com.mongodb.reactivestreams.client.MongoClient;
import org.reactivestreams.Subscriber;
import ru.study.chapter_03._03_news_service.dto.News;
import ru.study.chapter_03._03_news_service.dto.NewsLetter;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Введение в понятие обработчки Processor
 * NewsServicePublisher - доступ к БД для подготовки писем и последующей их рассылки
 */
public class NewsServicePublisher implements Publisher<NewsLetter> {

    final SmartMulticastProcessor processor;

    public NewsServicePublisher(MongoClient client, String categoryOfInterests) {
        ScheduledPublisher<NewsLetter> scheduler = new ScheduledPublisher<>(
                () -> new NewsPreparationOperator(
                        /*
                         Объявляется Supplier<? extends Publisher<NewsLetters>>, который предоставляет экземпляр
                         DBPublisher, завернутый в NewsPreparationOperator
                         */
                        new DBPublisher(
                                client.getDatabase("news")
                                        .getCollection("news", News.class),
                                categoryOfInterests
                        ),
                        "Some Digest"
                ),
                1, TimeUnit.DAYS
        );

        SmartMulticastProcessor processor = new SmartMulticastProcessor();

        /*
         SmartMulticastProcessor подписывается на ScheduledPublisher<NewsLetter>. Операция немедленно запускает
         планировщик, который подписывается на рассылку внутреннего издателя
         */
        scheduler.subscribe(processor);

        this.processor = processor;
    }

    public NewsServicePublisher(Consumer<SmartMulticastProcessor> setup) {
        this.processor = new SmartMulticastProcessor();

        setup.accept(processor);
    }

    @Override
    public void subscribe(Subscriber<? super NewsLetter> s) {
        processor.subscribe(s);
    }
}
