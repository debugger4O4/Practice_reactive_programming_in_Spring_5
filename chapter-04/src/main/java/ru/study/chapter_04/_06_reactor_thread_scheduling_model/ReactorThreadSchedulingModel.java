package ru.study.chapter_04._06_reactor_thread_scheduling_model;

/**
 * Модель планирования потоков выполения в Reactor.
 */
public class ReactorThreadSchedulingModel {

    /**
     * Оператор publishOn.
     */
    // Scheduler - интерфейс, представляющий рабочий поток или пул рабочих потоков в Project Reactor.
//    Scheduler scheduler = ...;
//                                          _
//    Flux.range(0, 100)                     |
//            .map(String::valurOf)          |> Главный поток выполнения.
//            .filter(s -> s.length() > 1)   |
//                                          _
              // После оператора publishOn() выполнение происходит в другом рабочем потоке планировщика Scheduler.
//            .publishOn(scheduler)
//                                          _
//            .map(this::calculateHash)      |
//            .map(this::doBusinessLogic)    |> Поток выполнения планировщика.
//            .subscribe()                   |
//                                          _
    /**
     * Оператор subscribeOn.
     */
//    ObjectMapper objectMapper = ...;
//    String json = "{ \"color\" : \"Black\", \"type\" : \"BMV\" }";
//    Mono.fromCallable(() -> objectMapper.readValue(json, Car.class))
//    ...
    /*
     Выполнение вызываемого объекта происходит в ходе выполнения метода subscribe. Это означает, что для смены рабочего
     потока выполнения, в котором происходит вызов Callable, нельзя использовать publishOn.
     */
//    public void subscribe(Subscriber actual) {
//        ...
//        Subscription subscription = ...;
//        try {
//            T t = callable.call();
//            if (t == null) {
//                subscription.onComplete();
//            } else {
//                subscription.onNext(t);
//                subscription.onComplete();
//            }
//        } catch (Throwable e) {
//            actual.onError(Operators.onOperatorError(e, actual.currentContext()));
//        }
//    }
    // subscribeOn() - определяет, в каком рабочем потоке будет выполнен этап подписки.
//    Scheduler scheduler = ...;
//    Mono.fromCallable(...)
//            .subscribeOn(scheduler)
//            .subscribe();
    /**
     * Оператор parallel.
     */
    // parallel() - разбивает один поток данных на несколько подпотоков и распределяет элементы между ними.
//    Flux.range(0, 10000)
//            .parallel()
            // runOn() - указывает, в каком планировщике должна происходить обработка элементов.
//            .runOn(Schedulers.parallel())
//            .map()
//            .filter()
//            .subscribe();
}
