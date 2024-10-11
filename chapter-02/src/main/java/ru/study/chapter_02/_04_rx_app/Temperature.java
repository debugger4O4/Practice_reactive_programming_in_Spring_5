package ru.study.chapter_02._04_rx_app;

/**
 * Переделка приложения с RxJava
 * Температура по Цельсию
 */
public final class Temperature {
    private final double value;

    public Temperature(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}