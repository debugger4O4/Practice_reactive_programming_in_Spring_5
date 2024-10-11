package ru.study.chapter_03._02_pull_vs_push._02_batched_pull_model;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface AsyncDatabaseClient {

    CompletionStage<List<Item>> getNextBatchAfterId(String id, int count);
}