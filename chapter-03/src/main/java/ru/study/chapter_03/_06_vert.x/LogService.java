package ru.study.chapter_03._06_vert.x;

import org.reactivestreams.Publisher;

public interface LogService {
    Publisher<String> stream();
}
