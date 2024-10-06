package ru.study.chapter_01.callbacks;

import ru.study.chapter_01.commons.Input;
import ru.study.chapter_01.commons.Output;

import java.util.function.Consumer;

/**
 * Имеет место блокирующая операция ввода/вывода в строке - бработка запроса запускает новый поток Thread.
 * Результат передается через функцию обратного вызова
 */
public class AsyncShoppingCardService implements ShoppingCardService {

    @Override
    public void calculate(Input value, Consumer<Output> c) {
        // Представлена операция блокировки, лучше предоставить ответ асинхронно
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            c.accept(new Output());
        }).start();
    }
}
