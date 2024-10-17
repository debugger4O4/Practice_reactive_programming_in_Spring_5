package ru.study.chapter_04._03_jet_stream_subscription;

/**
 * Подписка на реактивный поток
 */
public class JetStreamSubscription {
      // Самый простой способ подписаться на поток данных. Игнорирует любые сигналы.
//    subscribe();
      // Для каждого значения вызывается dataConsumer (сигнал onNext). Не обрабатывает сигналы onError и onComplete.
//    subscribe(Consumer<T> dataConsumer);
      // Позволяет обрабатывать сигнал onError.
//    subscribe(
//            Consumer<T> dataConsumer,
//            Consumer<Throwable> errorConsumer
//    );
      // Позволяет обрабатывыать сигнал onComplete.
//    subscribe(
//            Consumer<T> dataConsumer,
//            Consumer<Throwable> errorConsumer,
//            Runnable completeConsumer
//    );
      /*
       Позволяет получать все элементы из реактивного потока. Дает возможность управлять подпиской путем запроса
       требуемого объема данных.
       */
//    subscribe(
//            Consumer<T> dataConsumer,
//            Consumer<Throwable> errorConsumer,
//            Runnable completeConsumer
//            Consumer<Subscription> subscriptionConsumer
//    );
      /*
      Обобщенный способ подписки. Можно передать свою реализацию Subscriber с желаемым поведением. Редко используется
      на практике.
       */
//    subscriber(Subscriber<T> subscriber);
}
