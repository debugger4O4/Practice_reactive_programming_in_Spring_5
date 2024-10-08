package ru.study.chapter_02.observer;

/**
 * Итератор
 * @param <T>
 */
public interface Iterator<T> {
    // Получение элементов по одному
    T next();
    // Возможность сообщить об окончании коллекции
    boolean hasNext();
}
