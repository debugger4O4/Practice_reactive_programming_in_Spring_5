package ru.study.chapter_02.pub_sub_app;

/**
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
