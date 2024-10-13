package ru.study.chapter_03._03_news_service;

import org.reactivestreams.Subscriber;
import ru.study.chapter_03._03_news_service.dto.NewsLetter;

public interface ResubscribableErrorLetter {
    void resubscribe(Subscriber<? super NewsLetter> subscriber);
}
