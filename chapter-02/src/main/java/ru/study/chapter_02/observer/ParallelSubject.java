package ru.study.chapter_02.observer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Параллельная рассылка сообщений
 */
public class ParallelSubject implements Subject<String> {
    private final Set<Observer<String>> observers =
            new CopyOnWriteArraySet<>();

    public void registerObserver(Observer<String> observer) {
        observers.add(observer);
    }

    public void unregisterObserver(Observer<String> observer) {
        observers.remove(observer);
    }

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void notifyObservers(String event) {
        observers.forEach(observer ->
                /*
                Использование пула потоков для параллельной рассылки
                Чтобы узнать размер стека по умолчанию: java -XX:+PrintFlagsFinal -version | grep ThreadStackSize
                 */
                executorService.submit(
                        () -> observer.observe(event)
                )
        );
    }
}
