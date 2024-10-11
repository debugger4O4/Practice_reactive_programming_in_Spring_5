package ru.study.chapter_03._02_pull_vs_push._02_batched_pull_model;

import io.reactivex.rxjava3.core.Flowable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

public class DelayedFakeAsyncDatabaseClient implements AsyncDatabaseClient {

    @Override
    public CompletionStage<List<Item>> getNextBatchAfterId(String id, int count) {
        CompletableFuture<List<Item>> future = new CompletableFuture<>();

        Flowable.range(Integer.parseInt(id) + 1, count)
                .map(i -> new Item("" + i))
                .collectInto(new ArrayList<Item>(), ArrayList::add)
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribe(future::complete);

        return future;
    }
}
