package ru.study.chapter_02.observer;

/**
 * Observer - обощенный интерфейс. Регистрируется в Subject
 * @param <T>
 */
public interface Observer<T> {
    void observe(T event);
}
