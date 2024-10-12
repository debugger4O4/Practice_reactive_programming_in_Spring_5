package ru.study.chapter_03._02_pull_vs_push._04_push_pull_model;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

public class DelayedFakeAsyncDatabaseClient implements AsyncDatabaseClient {

    @Override
    public Publisher<Item> getStreamOfItems() {
        return Flowable.range(1, Integer.MAX_VALUE)
                .map(i -> new Item("" + i))
                .delay(50, TimeUnit.MILLISECONDS)
                .hide()
                .subscribeOn(Schedulers.io())
                .delaySubscription(100, TimeUnit.MILLISECONDS);
    }
}
