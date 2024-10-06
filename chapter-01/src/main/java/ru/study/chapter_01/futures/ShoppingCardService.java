package ru.study.chapter_01.futures;

import ru.study.chapter_01.commons.Input;
import ru.study.chapter_01.commons.Output;
import java.util.concurrent.Future;

/**
 * Организация асинхронного взаимодействия через Future - класс обертки, позволяющей проверить доступность
 * результата или заблокировать дальнейшее выполнение его ожидания
 */
public interface ShoppingCardService {
    Future<Output> calculate(Input value);
}
