package ru.study.chapter_02.rxjava;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.Subscriber;

import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * Производство и потребление потоков
 */
public class RxJavaExamplesTest {
    @Test
    @SuppressWarnings("Depricated")
    public void simpleRxJavaWorkflow() {
        /*
         Observable - реактивный класс аналог Subject из шаблона "Наблюдатель" - играет роль источника событий
         Генерирует одно строковое значение
         */
        Observable<String> observable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    /*
                    call() - вызывается при появлении каждого подписчика Subscriber
                    Subscriber - предоставляет механизм получения push-уведомлений от Observables и позволяет
                    вручную отписаться от этих Observables
                     */
                    @Override
                    public void call(Subscriber<? super String> sub) {
                        sub.onNext("Hello, reactive world!");
                        // Сигнал подписчику об окончании потока
                        sub.onCompleted();
                    }
                }
        );
    }

    @Test
    @SuppressWarnings("Depricated")
    public void simpleRxJavaWorkflowWithLambdas() {
        Observable.create(
                sub -> {
                    sub.onNext("Hello, reactive world!");
                    sub.onCompleted();
                }
        ).subscribe(
                System.out::println,
                System.err::println,
                () -> System.out.println("Done!")
        );
    }

    @Test
    public void creatingRxStreams() {
        Observable.just("1", "2", "3", "4");
        Observable.from(new String[]{"A", "B", "C"});
        Observable.from(Collections.<String>emptyList());

        Observable<String> hello = Observable.fromCallable(() -> "Hello ");
        Future<String> future =
                Executors.newCachedThreadPool().submit(() -> "World");
        Observable<String> world = Observable.from(future);

        /*
         concat() каждого из входящих потоков потребляет все элементы и отправляет их следующему наблюдателю. Входящие
         потоки будут обрабатываться до тех пор, пока не выполнится завершающая операция (onComplete(), onError())
         в порядке, соответствующем порядку аргументов concat()
         */
        Observable.concat(hello, world, Observable.just("!"))
                .forEach(System.out::print);
    }
}
