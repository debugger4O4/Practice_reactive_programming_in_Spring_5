package ru.study.chapter_02._02_rxjava;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;

import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.concurrent.TimeUnit.MILLISECONDS;


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

    /**
     * Генерация последовательности и асинхронных событий
     */
    // Генерирует асинхронные последователности, например через определенные интервалы времени
    @Test
    public void timeBasedSequenceExample() throws InterruptedException {
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(e -> System.out.println("Received: " + e));

        /*
         Если убрать Thread.sleep(...), то приложение завершится и ничего не выведется. Это объясняется тем, что события
         генерируются и потребляются отдельным фоновым потоком -> нужно вызвать sleep(), чтобы предотвратить
         преждевременное завершение главного потока
         */
        Thread.sleep(5000);
    }

    // Отмена подписки
    @Test
    public void managingSubscription() {
        AtomicReference<Subscription> subscription = new AtomicReference<>();
        subscription.set(Observable.interval(100, MILLISECONDS)
                .subscribe(e -> {
                    System.out.println("Received: " + e);
                    if (e >= 3) {
                        subscription.get().unsubscribe();
                    }
                }));

        do {
            // Сделать что-то полезное...
        } while (!subscription.get().isUnsubscribed());
    }

    @Test
    public void managingSubscription2() throws InterruptedException {
        /*
         CountDownLatch используется при ситуации, когда подписчик интересуется лишь частью событий и потребляет их, пока
         не получит внешний сигнал CountDownLatch(3)
         */
        CountDownLatch externalSignal = new CountDownLatch(3);

        /*
        Входящий поток генерирует события каждые 100мс и образует бесконечную последовательность - 0, 1, 2, 3...(3)
         */
        Subscription subscription = Observable
                .interval(100, MILLISECONDS)
                .subscribe(System.out::println);

        // Приводит к отмене подписки
        externalSignal.await(450, MILLISECONDS);
        // Отписка
        subscription.unsubscribe();
    }

    /**
     * Преобразование потоков и диаграммы Marble
     * Опервтор zip - объединение элементов из двух параллельных потоков
     */
    @Test
    public void zipOperatorExample() {
        Observable.zip(
                Observable.just("A", "B", "C"),
                Observable.just("1", "2", "3"),
                (x, y) -> x + y
        ).forEach(System.out::println);
    }

    /*
    Асинхронный рабочий процесс
     */
    @Test
    public void deferSynchronousRequest() throws Exception {
        String query = "query";
        Observable.fromCallable(() -> doSlowSyncRequest(query))
                /*
                subscribeOn() определяет, какой планировщик Scheduler(реактивный аналог ExecutorService в java)
                управляет обработкой потока данных
                 */
                .subscribeOn(Schedulers.io())
                .subscribe(this::processResult);

        Thread.sleep(1000);
    }

    private String doSlowSyncRequest(String query) {
        return "result";
    }

    private void processResult(String result) {
        System.out.println(Thread.currentThread().getName() + ": " + result);
    }
}
