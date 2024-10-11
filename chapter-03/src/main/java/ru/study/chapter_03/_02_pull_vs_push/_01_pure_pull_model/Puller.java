package ru.study.chapter_03._02_pull_vs_push._01_pure_pull_model;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Модели обмена PULL и PUSH
 * Служба взаимодействия с БД асинхронным и неблокирующим способом
 */
public class Puller {

    // Асинхронные, неблокирующие взаимодействия с внешней БД
    final AsyncDatabaseClient dbClient = new DelayedFakeAsyncDatabaseClient();

    // Асинхронный контракт
    public CompletionStage<Queue<Item>> list(int count) {
        BlockingQueue<Item> storage = new ArrayBlockingQueue<>(count);

        // Сохранение принятых значений для ручной отправки в собранную очередь
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
         Асинхронное выполнение запроса и получение результата
         getNextAfterId() - получение элементов по очереди - это затратно по времени
         */
        dbClient.getNextAfterId(elementId)
                .thenAccept(item -> {
                    if (isValid(item)) {
                        queue.offer(item);

                         /*
                         Если количество собранных элементов соответствует заданному, элементы посылаются
                         вызывающему коду и метод завершается
                         */
                        if (queue.size() == count) {
                            resultFuture.complete(queue);
                            return;
                        }
                    }

                    pull(item.getId(), queue, resultFuture, count);
                });
    }

    boolean isValid(Item item) {
        return Integer.parseInt(item.getId()) % 2 == 0;
    }
}
