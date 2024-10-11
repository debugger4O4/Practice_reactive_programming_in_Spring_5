package ru.study.chapter_03._02_pull_vs_push._03_push_model;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class DelayedFakeAsyncDatabaseClient implements AsyncDatabaseClient {

    @Override
    public Observable<Item> getStreamOfItems() {
        return Observable.range(1, Integer.MAX_VALUE)
                .map(i -> new Item("" + i))
                .delay(50, TimeUnit.MILLISECONDS)
                .delaySubscription(100, TimeUnit.MILLISECONDS);
    }
}