package ru.study.chapter_03._02_pull_vs_push._04_push_pull_model;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.function.Predicate;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class TakeFilterOperator<T> implements Publisher<T> {

    private final Publisher<T> source;
    private final int take;
    private final Predicate<T> predicate;

    public TakeFilterOperator(Publisher<T> source, int take, Predicate<T> predicate) {
        this.source = source;
        this.take = take;
        this.predicate = predicate;
    }

    // Поддержка дополнительной логики потока
    public void subscribe(Subscriber s) {
        source.subscribe(new TakeFilterInner<>(s, take, predicate));
    }

    /**
     * Применение Subscriber и Subscription - реализация промежуточного этапа преобразования
     */
    static final class TakeFilterInner<T> implements Subscriber<T>, Subscription {

        // actual - фактический подписчик
        final Subscriber<T> actual;
        final int take;
        final Predicate<T> predicate;
        final Queue<T> queue;

        Subscription current;
        int remaining;
        int filtered;
        Throwable throwable;
        boolean done;

        volatile long requested;
        static final AtomicLongFieldUpdater<TakeFilterInner> REQUESTED =
                AtomicLongFieldUpdater.newUpdater(TakeFilterInner.class, "requested");

        volatile int wip;
        static final AtomicIntegerFieldUpdater<TakeFilterInner> WIP =
                AtomicIntegerFieldUpdater.newUpdater(TakeFilterInner.class, "wip");

        TakeFilterInner(Subscriber<T> actual, int take, Predicate<T> predicate) {
            this.actual = actual;
            this.take = take;
            this.remaining = take;
            this.predicate = predicate;
            this.queue = new ConcurrentLinkedQueue<>();
        }


        public void onSubscribe(Subscription current) {
            if (this.current == null) {
                this.current = current;

                this.actual.onSubscribe(this);
                if (take > 0) {
                    // Отправка запроса удаленной БД, что обычно происходит в первом вызове onSubscribe()
                    this.current.request(take);
                } else {
                    onComplete();
                }
            } else {
                current.cancel();
            }
        }

        // Принятие очередного метода для обработки
        public void onNext(T element) {
            if (done) {
                return;
            }

            long r = requested;
            Subscriber<T> a = actual;
            Subscription s = current;

            // Процесс обработки элемента
            if (remaining > 0) {
                boolean isValid = predicate.test(element);
                boolean isEmpty = queue.isEmpty();

                /*
                 Если число элементов remaining, которые необходимо извлеч > 0, фактический подписчик Subscriber запросил
                 данные (допустим, элемент) и в очереди отсутствуют другие элементы, то этот элемент можно послать
                 непосредственно нижестоящему подписчику
                 */
                if (isValid && r > 0 && isEmpty) {
                    a.onNext(element);
                    remaining--;

                    REQUESTED.decrementAndGet(this);
                    if (remaining == 0) {
                        s.cancel();
                        onComplete();
                    }
                /*
                Если данные не были запрошены или в очереди имеются другие элементы, то поместить новый элемент в очередь
                и доставить его позже
                 */
                } else if (isValid && (r == 0 || !isEmpty)) {
                    queue.offer(element);
                    remaining--;

                    if (remaining == 0) {
                        s.cancel();
                        onComplete();
                    }
                    drain(a, r);
                // Если элемент недопустим, то учелиить счетчик отфильтрованных элементов
                } else if (!isValid) {
                    filtered++;
                }
             // Если remaining == 0, то отмена подписка - cancel() и закрытие потока - onComplete()
            } else {
                s.cancel();
                onComplete();
            }

            /*
            Механизм запроса дополнительных данных
            Если filtered достигло установленного предела, то запросить дополнительную порцию данных, не блокируя весь
            процесс
             */
            if (filtered > 0 && remaining / filtered < 2) {
                s.request(take);
                filtered = 0;
            }
        }

        @Override
        public void onError(Throwable t) {
            if (done) {
                return;
            }

            done = true;

            if (queue.isEmpty()) {
                actual.onError(t);
            } else {
                throwable = t;
            }
        }

        @Override
        public void onComplete() {
            if (done) {
                return;
            }

            done = true;

            if (queue.isEmpty()) {
                actual.onComplete();
            }
        }

        @Override
        public void request(long n) {
            if (n <= 0) {
                onError(new IllegalArgumentException(
                        "Spec. Rule 3.9 - Cannot request a non strictly positive number: " + n
                ));
            }

            drain(actual, SubscriptionUtils.request(n, this, REQUESTED));
        }

        @Override
        public void cancel() {
            if (!done) {
                current.cancel();
            }

            queue.clear();
        }

        void drain(Subscriber<T> a, long r) {
            if (queue.isEmpty() || r == 0) {
                return;
            }

            int wip;

            if ((wip = WIP.incrementAndGet(this)) > 1) {
                return;
            }

            int c = 0;
            boolean empty;

            for (; ; ) {
                T e;
                while (c != r && (e = queue.poll()) != null) {
                    a.onNext(e);
                    c++;
                }


                empty = queue.isEmpty();
                r = REQUESTED.addAndGet(this, -c);
                c = 0;

                if (r == 0 || empty) {
                    if (done && empty) {
                        if (throwable == null) {
                            a.onComplete();
                        } else {
                            a.onError(throwable);
                        }
                        return;
                    }

                    wip = WIP.addAndGet(this, -wip);

                    if (wip == 0) {
                        return;
                    }
                } else {
                    wip = this.wip;
                }
            }
        }
    }
}
