package ru.study.chapter_01._03_callbacks;

import ru.study.chapter_01._02_commons.Input;
import ru.study.chapter_01._02_commons.Output;

import java.util.function.Consumer;

/**
 * Предполагает отсутствие блокирующих операций. Ввод/вывод не производится - результат можно
 * вернуть немедленно с помощью функции обратного вызова
 */
public class SyncShoppingCardService implements ShoppingCardService {

    @Override
    public void calculate(Input value, Consumer<Output> c) {
        // Никакой операции блокировки, лучше сразу дать ответ
        c.accept(new Output());
    }
}
