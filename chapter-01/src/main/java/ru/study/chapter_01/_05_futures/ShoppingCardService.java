package ru.study.chapter_01._05_futures;

import ru.study.chapter_01._02_commons.Input;
import ru.study.chapter_01._02_commons.Output;
import java.util.concurrent.Future;

/**
 * Организация асинхронного взаимодействия через Future - класс обертки, позволяющей проверить доступность
 * результата или заблокировать дальнейшее выполнение его ожидания
 */
public interface ShoppingCardService {
    Future<Output> calculate(Input value);
}
