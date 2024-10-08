package ru.study.chapter_02.search_engine;

import java.net.URL;

@SuppressWarnings("unused")
public interface IterableSearchEngine {
    /*
     Обработка поиска итеративно. Сервер будет искать следующий результат, пока клиент потребляет предыдущий. Обычно
     поиск на сервере возвращает не один, а целую группу результатов(например, 100). Такое решение называется курсором
     и часто используется в БД. С точки зрения клиента курсор - это итератор
     */
    Iterable<URL> search(String query, int limit);
}
