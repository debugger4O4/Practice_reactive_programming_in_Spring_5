package ru.study.chapter_03._02_pull_vs_push._01_pure_pull_model;

import java.util.concurrent.CompletionStage;

public interface AsyncDatabaseClient {

    CompletionStage<Item> getNextAfterId(String id);
}