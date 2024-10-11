package ru.study.chapter_02._01_observer;

/**
 * Observer - обобщенный интерфейс. Регистрируется в Subject
 * @param <T>
 */
public interface Observer<T> {
    void observe(T event);
}
