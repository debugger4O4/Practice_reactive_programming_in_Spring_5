package ru.study.chapter_02.observer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Реализация Subject<String>, которая генерирует сообщения типа String
 */
public class ConcreteSubject implements Subject<String> {

    /*
    Хранилище для наблюдателей, подписавшихся на получение уведомлений
    CopyOnWriteArraySet - потокобезопасная реализация Set
     */
    private final Set<Observer<String>> observers = new CopyOnWriteArraySet<>();

    // Оформление подписки
    public void registerObserver(Observer<String> observer) {
        observers.add(observer);
    }

    // Отмена подписки
    public void unregisterObserver(Observer<String> observer) {
        observers.remove(observer);
    }

    // Рассылка событий
    public void notifyObservers(String event) {
        /*
         Обходит список наблюдателей и вызывает observe() каждого зарегистрировавшегося экземпляра Observer,
         передавая ему актуальное событие
         */
        observers.forEach(observer -> observer.observe(event));
    }
}
