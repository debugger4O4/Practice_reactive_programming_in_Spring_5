package ru.study.chapter_04._05_reactive_streams_life_cycle._03_execution;

/**
 * Выполнение.
 */
public class Execution {

    // Процесс передачи подписки Subscription каждому подписчику Subscriber.
    MapSubscriber(FilterSubscriber(Subscriber)).onSubscribe(new ArraySubscription()) {
        FilterSubscriber(Subscriber).onSubscribe(new MapSubscription(ArraySubscription(...))) {
            Subscriber.onSubscribe(FilterSubscription(MapSubscription(ArraySubscription(...)))) {
                // Здесь выполняется запрос данных.
            }
        }
    }

    /*
     После передачи подписки всем подписчикам в цепочке и каждый подписчик упакует ее в конкретное представление,
     получится пирамида.
     */
    FilterSubscription(
            MapSubscription (
                    ArraySubscription()
            )
    )

    // Начало получения элементов, кога последний подписчик получит цепочку подписок.
    FilterSubscription(MapSubscription(ArraySubscription(...))).request(10) {
        MapSubscription(ArraySubscription(...)).request(10) {
            ArraySubscription(...).request(10) {
                // Начало отправки данных.
            }
        }
    }

    /*
     Передача элементов подписчикам, когда все подписчики передадут запрошенное количество элементов и
     ArraySubscription получит его.
     */
    ...
    ArraySubscription.request(10) {
        MapSubscriber(FilterSubscriber(Subscriber)).onNext(1) {
            // Применение отображения.
            FilterSubscriber(Subscriber).onNext("1") {
                // Если элемент не соответствует фильтру, то запросить дополнительный элемент.
                MapSubscription(ArraySubscription(...)).request(1) {
                    ...
                }
            }
        }

        MapSubscriber(FilterSubscriber(Subscriber)).onNext(20) {
            // Применение отображения.
            FilterSubscriber(Subscriber).onNext("20") {
                // Если элемент соответствует фильтру, то передать его нижестоящему подписчику.
                Subscriber.onNext("20") {
                    ...
                }
            }
        }
    }
}
