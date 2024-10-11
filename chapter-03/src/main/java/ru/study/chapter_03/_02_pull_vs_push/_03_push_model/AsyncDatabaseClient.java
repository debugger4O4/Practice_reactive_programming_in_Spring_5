package ru.study.chapter_03._02_pull_vs_push._03_push_model;

import io.reactivex.rxjava3.core.Observable;

public interface AsyncDatabaseClient {

    Observable<Item> getStreamOfItems();
}