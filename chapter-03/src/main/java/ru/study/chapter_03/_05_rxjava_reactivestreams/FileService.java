package ru.study.chapter_03._05_rxjava_reactivestreams;

import org.reactivestreams.Publisher;

public interface FileService {

    void writeTo(String file, Publisher<String> content);
}
