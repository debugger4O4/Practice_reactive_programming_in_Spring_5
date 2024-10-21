package ru.study.chapter_04._05_reactive_streams_life_cycle._01_assembly_stage._02_subscription_stage;

/**
 * Этап подписки.
 */
public class SubscriptionStage {

    // Подписка.
//    ...
//    filteredFlux.subscribe(...);

    // Распространение Subscriber по цепочке издателей на этапе подписки.
//    filterFlux.subscribe(Subscriber) {
//        mapFlux.subscribe(new FilterSubscriber(Subscriber)) {
//            arrayFlux.subscribe(new MapSubscriber(FilterSubscriber(Subscriber))) {
                // Здесь начинается передача фактических элементов.
//            }
//        }
//    }

    // Последовательность вышестоящего метода, когда его выполнение достигнет строчки с комментариями.
//    ArraySubscriber(
//            MapSubscriber(
//                    FilterSubscriber(
//                            Subscriber
//                    )
//            )
//    )
}
