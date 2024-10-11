package ru.study.chapter_02._03_search_engine;

import rx.Observable;

import java.net.URL;

@SuppressWarnings("unused")
public interface RxSearchEngine {
    /*
    Задействовав RxJava можно организовать асинхронную обработку, получив возможность реагировать на каждое прибывающее
    событие. Кроме того, клиент может отменить подписку возовом unsubscribe() в любой момент и сократить объем работы,
    выполнямой службой
     */
    Observable<URL> search(String query);
}
