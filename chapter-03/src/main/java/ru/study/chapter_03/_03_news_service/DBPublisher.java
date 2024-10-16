package ru.study.chapter_03._03_news_service;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.FindPublisher;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import ru.study.chapter_03._03_news_service.dto.News;

import java.util.Date;

/**
 * Отчет за извлечение новостей из БД
 * <b>
 * Драйвер MongoDB с поддержкой Reactive Streams
 */
public class DBPublisher implements Publisher<News> {
    private final MongoCollection<News> collection;
    private final String category;

    public DBPublisher(MongoCollection<News> collection, String category) {
        this.collection = collection;
        this.category = category;
    }

    @Override
    public void subscribe(Subscriber<? super News> s) {
        /*
         FindPublisher предлагает гибкий API, позволяющий создавать выполняемые запросы с использование функционального
         стиля программирования
         */
        FindPublisher<News> findPublisher = collection.find(News.class);
        // publishedOn - начальная дата для поиска новостей
        findPublisher.sort(Sorts.descending("publishedOn"))
                .filter(Filters.and(
                        Filters.eq("category", category),
                        Filters.gt("publishedOn", today())
                ))
                .subscribe(s);
    }

    private Date today() {
        Date date = new Date();
        return new Date(date.getYear(), date.getMonth(), date.getDate());
    }
}