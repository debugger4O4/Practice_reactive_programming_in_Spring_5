package ru.study.chapter_02._03_search_engine;

import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public interface FutureSearchEngine {
    /*
    Единственный недостаток решения на основе итератора - блокировка клиентского потока в режиме активного ожидания,
    когда придет время получить новую порцию данных, иначе говоря, посредством Iterable клиент и служа играют в пинг-понг
    Служба поиска может вернуть CompletableFuture и таким образом стать асинхронной, но здесь снова получаем все или
    ничего, потому что CompletableFuture может хранить только одно значение, даже если это список результатов
     */
    CompletableFuture<List<URL>> search(String query, int limit);
}