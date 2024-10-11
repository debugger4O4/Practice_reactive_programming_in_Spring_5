package ru.study.chapter_02._01_observer;

/**
 * Subject генерирует события или может сам вызываться другими компонентами
 * @param <T>
 */
public interface Subject<T> {
    void registerObserver(Observer<T> observer);

    void unregisterObserver(Observer<T> observer);

    void notifyObservers(T event);
}
