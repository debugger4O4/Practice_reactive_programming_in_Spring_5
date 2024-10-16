package ru.study.chapter_03._06_vert.x;

import io.reactivex.rxjava3.core.Flowable;
import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

public class MockLogService implements LogService {

    @Override
    public Publisher<String> stream() {
        return  Flowable.interval(300, TimeUnit.MILLISECONDS)
                .map(i -> "[" + System.nanoTime() + "] [LogServiceApplication] " + "[Thread " + Thread.currentThread() + "] Some loge here " + i + "\n");
    }
}
