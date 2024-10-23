package ru.study.chapter_04;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Контекст.
 */
public class ThreadLocalProblemShowcaseTest {

    @Test(expected = NullPointerException.class)
    public void shouldFailDueToDifferentThread() {
        // Объявление ThreadLocal.
        ThreadLocal<Map<Object, Object>> threadLocal = new ThreadLocal<>();
        // Подготовка ThreadLocal для дальнейшего использования.
        threadLocal.set(new HashMap<>());

        // Генерация числовых элементов от 0 до 9.
        Flux.range(0, 10)
                /*
                 Генерация случайных чисел с помощью Гауссовского распределения - это симметричное распределение
                 вероятности, центрированное на среднем значении. Оно указывает на то, что данные, находящиеся вблизи
                 среднего значения, встречаются чаще, чем данные, находящиеся далеко от него.
                 Помещение сгенерированного значения в ассоциативный массив.
                 */
                .doOnNext(k -> threadLocal.get().put(k, new Random(k).nextGaussian()))
                // Передача выполнения в другой поток Thread.
                .publishOn(Schedulers.parallel())
                /*
                Отображение числа из потока в случайное значение, сгенерированное ранее и сохраненное в ассоциативном
                массиве ThreadLocal. В этот момент получится NullPointerException потому что ассоциативный массив,
                созданный в главном потоке выполнения, недоступен в другом потоке выполнения.
                 */
                .map(k -> threadLocal.get().get(k))
                .blockLast();

        // Решение проблемы NullPointerException в shouldFailDueToDifferentThread().
//        Flux.range(0, 10)
//                .flatMap(k ->
                        // subscriberContext() - осуществление доступа к экземпляру Context в текущем потоке
//                        Mono.subscriberContext()
                                // Обрщение к реализации Context в Reactor.
//                                .doOnNext(context -> {
                                    // Обращение к экземпляру Map экземпляра Context.
//                                    Map<Object, Object> map = context.get("randoms");
                                    // Сохранение сгенерированных значений.
//                                    map.put(k, new Random(k).nextGaussian());
//                                })
                                // Возвращение начального параметра flatMap.
//                                .thenReturn(k)
//                )
//                .publishOn(Schedulers.parallel())
//                .flatMap(k ->
                        // Снова обращение к экземпляру Context, уже после выполнения потока выполнения.
//                        Mono.subscriberContext()
//                                .map(context -> {
                                    // Благополучное извлечение хранимого ассоциативного массива.
//                                    Map<Object, Object> map = context.get("randoms");
                                    // Получение сгенерированного значения.
//                                    return map.get(k);
//                                })
//                )
                /*
                 Добавление нового ассоциативного массива "randoms" во входящий поток в составе нового экземпляра
                 Context.
                 */
//                .subscriberContext(context ->
//                        context.put("randoms", new HashMap<>())
//                )
//                .block();
    }
}
