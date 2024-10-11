package ru.study.chapter_03._01_conversion_problem;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class FakeAsyncDatabaseClient implements AsyncDatabaseClient {

    @Override
    public <T> CompletionStage<T> store(CompletionStage<T> stage) {
        return stage.thenCompose(e -> CompletableFuture.supplyAsync(() -> e));
    }
}
