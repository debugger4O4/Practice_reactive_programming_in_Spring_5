package ru.study.chapter_03._02_pull_vs_push._02_batched_pull_model;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class Puller {

    final AsyncDatabaseClient dbClient = new DelayedFakeAsyncDatabaseClient();
    public CompletionStage<Queue<Item>> list(int count) {
        BlockingQueue<Item> storage = new ArrayBlockingQueue<>(count);
        CompletableFuture<Queue<Item>> result = new CompletableFuture<>();

        pull("1", storage, result, count);

        return result;
    }

    void pull(
            String elementId,
            Queue<Item> queue,
            CompletableFuture resultFuture,
            int count
    ) {

        /*
        getNextBatchAfterId() - получение пакета элементов по очереди - менее затратно по времени по сравнению с
        getNextAfterId()
         */
        dbClient.getNextBatchAfterId(elementId, count)
                .thenAccept(items -> {
                    for (Item item : items) {
                        if (isValid(item)) {
                            queue.offer(item);
                            if (queue.size() == count) {
                                resultFuture.complete(queue);
                                return;
                            }
                        }
                    }

                    pull(items.get(items.size() - 1)
                            .getId(), queue, resultFuture, count);
                });
    }

    boolean isValid(Item item) {
        return Integer.parseInt(item.getId()) % 2 == 0;
    }
}
