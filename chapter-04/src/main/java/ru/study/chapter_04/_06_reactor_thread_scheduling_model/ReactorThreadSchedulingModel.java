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
}
