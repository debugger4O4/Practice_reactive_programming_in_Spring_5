package ru.study.chapter_03._05_rxjava_reactivestreams;

import org.reactivestreams.Publisher;

/**
 * Изменения в RxJava
 */
public interface LogService {
    Publisher<String> stream();
}
