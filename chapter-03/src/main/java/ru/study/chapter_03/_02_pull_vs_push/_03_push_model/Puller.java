package ru.study.chapter_03._02_pull_vs_push._03_push_model;

import io.reactivex.rxjava3.core.Observable;

public class Puller {

    final AsyncDatabaseClient dbClient = new DelayedFakeAsyncDatabaseClient();

    /*
    getStreamOfItems() - подписка на поток БД 1 раз. Сразу после выполнение всех требований посылается сигнал отмены
    и соединение с БД закрывается
     */
    public Observable<Item> list(int count) {
        return dbClient.getStreamOfItems()

                // Фильтрация
                .filter(this::isValid)

                // Объем данных, указанный вызываемым кодом
                .take(count);
    }

    boolean isValid(Item item) {
        return Integer.parseInt(item.getId()) % 2 == 0;
    }
}
