package ru.study.chapter_05._03_reactive_type_conversion_support;

/**
 * Поддержка преобразования реактивных типов.
 */
public class ReactiveAdapter {
    // Преобразование любых типов в Publisher  и обратно.
    ...
    <T> Publisher<T> toPublisher(@Nullable Object source) {...}
    Object fromPublisher(Publisher<?> publisher) {...}
}
