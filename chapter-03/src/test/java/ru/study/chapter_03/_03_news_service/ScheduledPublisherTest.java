package ru.study.chapter_03._03_news_service;

import io.reactivex.rxjava3.core.Flowable;
import org.reactivestreams.Publisher;
import org.reactivestreams.tck.PublisherVerification;
import org.reactivestreams.tck.TestEnvironment;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ScheduledPublisherTest extends PublisherVerification<Long> {

    public ScheduledPublisherTest() {
        super(new TestEnvironment(2000, 100));
    }

    @Override
    public Publisher<Long> createPublisher(long elements) {
        AtomicLong counter = new AtomicLong();

        ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(1);
        scheduledExecutor.setRemoveOnCancelPolicy(true);

        return Flowable.fromPublisher(new ScheduledPublisher<>(
                () -> Flowable.just(counter.getAndIncrement()).delaySubscription(100, TimeUnit.MILLISECONDS),
                100,
                TimeUnit.MILLISECONDS,
                scheduledExecutor
        )).take(elements);
    }

    @Override
    public Publisher<Long> createFailedPublisher() {
        return new ScheduledPublisher<>(
                () -> Flowable.error(new RuntimeException()),
                50,
                TimeUnit.MILLISECONDS,
                Executors.newSingleThreadScheduledExecutor()
        );
    }

}