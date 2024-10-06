package ru.study.chapter_01.callbacks;

import ru.study.chapter_01.commons.Input;
import ru.study.chapter_01.commons.Output;

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
