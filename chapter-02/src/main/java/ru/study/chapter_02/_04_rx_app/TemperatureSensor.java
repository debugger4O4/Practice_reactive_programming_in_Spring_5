package ru.study.chapter_02._04_rx_app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.util.Random;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Реализация бизнес-логики
 */
@Component
public class TemperatureSensor {
    private static final Logger log = LoggerFactory.getLogger(TemperatureSensor.class);
    private final Random rnd = new Random();

    // dataStream единственный поток Observable
    private final Observable<Temperature> dataStream =
            Observable
                    .range(0, Integer.MAX_VALUE)
                    /*
                    concatMap() принимает функцию f и преобразует эелемент ignore в элементы потока Observable, применяя
                    f к каждому элементу входного потока и объединяя их друг за другом в выходной поток
                     */
                    .concatMap(ignore -> Observable
                            // just() - создание нового потока с единственным элементом
                            .just(1)
                            .delay(rnd.nextInt(5000), MILLISECONDS)
                            // Извлечение значения, применяя преобразование map()
                            .map(ignore2 -> this.probe()))
                    /*
                     publish() рассылает события из одного исходного потока во все потоки, связанные с подписчиками
                     publish() возвращает особую разновидность Observable - ConnectableObservable, который поддержиает
                     refCount()
                     */
                    .publish()
                    /*
                    refCount() создает подписку на общий входящий поток, только когда имеется хотя бы одна исходящая
                    подписка
                     */
                    .refCount();

    public Observable<Temperature> temperatureStream() {
        return dataStream;
    }

    private Temperature probe() {
        double actualTemp = 16 + rnd.nextGaussian() * 10;
        log.info("Asking sensor, sensor value: {}", actualTemp);
        return new Temperature(actualTemp);
    }
}
