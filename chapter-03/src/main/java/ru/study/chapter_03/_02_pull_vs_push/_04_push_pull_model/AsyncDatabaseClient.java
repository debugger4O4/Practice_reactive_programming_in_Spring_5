package ru.study.chapter_03._02_pull_vs_push._04_push_pull_model;

import org.reactivestreams.Publisher;

public interface AsyncDatabaseClient {

    Publisher<Item> getStreamOfItems();
}
