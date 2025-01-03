package ru.study.chapter_01._04_completion_stage;

import ru.study.chapter_01._02_commons.Input;
import ru.study.chapter_01._02_commons.Output;

import java.util.concurrent.CompletionStage;

/**
 * Блокировка текущего потока и синхронизация с внешним выполнением ухудшает масштабируемость.
 * CompletionStage - решение проблемы в java 8. Эти классы поддерживают promise-подобные API
 */
public interface ShoppingCardService {
    /*
     CompletionStage - класс обертки, похожий на Future, но позволяющий обрабаывать и возвращать результат
     в декларативной манере функционального программирования
     */
    CompletionStage<Output> calculate(Input value);
}
