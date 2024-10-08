package ru.study.chapter_02.observer;

/**
 * Простой наблюдатель B
 */
public class ConcreteObserverB implements Observer<String> {
    @Override
    public void observe(String event) {
        System.out.println("Observer B: " + event);
    }
}
