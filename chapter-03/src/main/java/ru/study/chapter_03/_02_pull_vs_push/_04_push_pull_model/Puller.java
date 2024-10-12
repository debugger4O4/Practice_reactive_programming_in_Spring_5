package ru.study.chapter_03._02_pull_vs_push._04_push_pull_model;

import org.reactivestreams.Publisher;

/**
 * Основные положения стандарта Reactive Streams
 */
public class Puller {

    final AsyncDatabaseClient dbClient = new DelayedFakeAsyncDatabaseClient();

    public Publisher<Item> list(int count) {
        Publisher<Item> source = dbClient.getStreamOfItems();
        TakeFilterOperator<Item> takeFilter =
                new TakeFilterOperator<>(source, count, this::isValid);
        // takeFilter расширяет Publisher
        return takeFilter;
    }

    boolean isValid(Item item) {
        return Integer.parseInt(item.getId()) % 2 == 0;
    }
}
