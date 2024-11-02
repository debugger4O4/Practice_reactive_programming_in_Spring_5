package ru.study.chapter_04._04_filtering_reactive_sequences;

/**
 * Фильтрация реактивных последовательностей.
 */
public class FilteringReactiveSequences {
    Mono<?> startCommand = ...
    Mono<?> stopCommand = ...
    Flux<UserEvent> streamOfData = ...

    streamOfData // 1 2 3 4 5 6
              // Пропуск элементов.
            .skipUntilOther(startCommand) // 2 3 4
              // Извлечение элементов.
            .takeUntilOther(stopCommand) // 2 3 4
            .subscribe(System.out::println); // 2 3 4
}
