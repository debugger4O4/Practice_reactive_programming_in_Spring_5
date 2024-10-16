package ru.study.chapter_03._04_jdk9;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.reactivestreams.FlowAdapters;
import org.reactivestreams.Publisher;
import ru.study.chapter_03._03_news_service.NewsServicePublisher;
import ru.study.chapter_03._03_news_service.NewsServiceSubscriber;
import ru.study.chapter_03._03_news_service.dto.NewsLetter;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;

/**
 * JDK9
 */
public class AdapterExample {

    public static void main(String[] args) {
        /*
         Flow.Publisher происходит из JDK 9,  FlowAdapters.toFlowPublisher() происходит из оригинальной библиотеки
         org.reactivestreams для преобразования Flow.Publisher в org.reactivestreams.Publisher
         */
        Flow.Publisher jdkPublisher = FlowAdapters.toFlowPublisher(new NewsServicePublisher(smp ->
                Flowable.intervalRange(0, 10, 0, 10, TimeUnit.MILLISECONDS, Schedulers.computation())
                        .map(e -> NewsLetter.template()
                                .title(String.valueOf(e))
                                .digest(Collections.emptyList())
                                .build())
                        .subscribe(smp)
        ));
        Publisher external = FlowAdapters.toPublisher(jdkPublisher);
        Flow.Publisher jdkPublisher2 = FlowAdapters.toFlowPublisher(
                /*
                 Обратное преобразование org.reactivestreams.Publisher в Flow.Publisher с исползованием
                 FlowAdapters.toPublisher()
                 */
                external
        );

        NewsServiceSubscriber newsServiceSubscriber = new NewsServiceSubscriber(2);
        jdkPublisher2.subscribe(FlowAdapters.toFlowSubscriber(newsServiceSubscriber));



        while (true) {
            Optional<NewsLetter> letterOptional = newsServiceSubscriber.eventuallyReadDigest();

            if (letterOptional.isPresent()) {
                NewsLetter letter = letterOptional.get();
                System.out.println(letter);

                if (letter.getTitle().equals("9")) {
                    break;
                }
            }
        }
    }
}
