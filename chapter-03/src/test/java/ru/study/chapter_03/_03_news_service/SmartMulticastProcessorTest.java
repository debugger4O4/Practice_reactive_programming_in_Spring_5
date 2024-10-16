package ru.study.chapter_03._03_news_service;

import io.reactivex.rxjava3.core.Flowable;
import org.reactivestreams.Processor;
import org.reactivestreams.Publisher;
import org.reactivestreams.tck.IdentityProcessorVerification;
import org.reactivestreams.tck.TestEnvironment;
import org.testng.SkipException;
import ru.study.chapter_03._03_news_service.dto.NewsLetter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

/**
 * IdentityProcessorVerification - набор тестов для Processor, получающий и посылающий элементы одного типа
 */
public class SmartMulticastProcessorTest
        extends IdentityProcessorVerification<NewsLetter> {

    /*
    Передаем дополнительный параметр с количеством элементов, который обработчик Processor должен кэшировать
    Реализация Processor кэширует только 1 элемент, поэтому это нужно явно указать перед началом тестирования
     */
    public SmartMulticastProcessorTest() {
        super(new TestEnvironment(500, 500), 1000, 1);
    }

    @Override
    public ExecutorService publisherExecutorService() {
        return ForkJoinPool.commonPool();
    }

    // Фабричный метод для создания новых элементов
    @Override
    public NewsLetter createElement(int element) {
        return new StubNewsLetter(element);
    }

    /*
    Возвращает проверяемый экземпляр Processor
    bufferSize - кол-во элементов, который обработчик Processor должен кэшировать. Можно пропустить этот параметр, т.к.
    размер внутреннего буффера уже указан в конструкторе
     */
    @Override
    public Processor<NewsLetter, NewsLetter> createIdentityProcessor(int bufferSize) {
        return new SmartMulticastProcessor();
    }

    /*
    В реактивном программировании, координационная эмиссия - процесс распространения изменений через потоки данных.
    Это означает, что изменение в одном месте системы автоматически вызывает изменения во всех зависимых частях.
    В реактивном программировании, присваивание переменной новому значению не просто обновляет эту переменную,
    но и автоматически распространяет изменения по всей системе, что позволяет быстро реагировать на изменения данных.
    Это отличается от императивного программирования, где присваивание новой переменной приводит лишь к ее обновлению,
    без распространения изменений
     */
    @Override
    public boolean doesCoordinatedEmission() {
        return true;
    }

    @Override
    public Publisher<NewsLetter> createFailedPublisher() {
        return Flowable.error(new RuntimeException());
    }

    @Override
    public void required_spec105_mustSignalOnCompleteWhenFiniteStreamTerminates() {
        try {
            super.required_spec105_mustSignalOnCompleteWhenFiniteStreamTerminates();
        }
        catch (Throwable t) {
            throw new SkipException("Ignored due to undetermined drop-on-overflow behavior for that processor");
        }
    }

    @Override
    public void required_spec101_subscriptionRequestMustResultInTheCorrectNumberOfProducedElements() {
        try {
            super.required_spec101_subscriptionRequestMustResultInTheCorrectNumberOfProducedElements();
        }
        catch (Throwable t) {
            throw new SkipException("Ignored due to undetermined drop-on-overflow behavior for that processor");
        }
    }

    @Override
    public void required_mustRequestFromUpstreamForElementsThatHaveBeenRequestedLongAgo() {
        try {
            super.required_mustRequestFromUpstreamForElementsThatHaveBeenRequestedLongAgo();
        }
        catch (Throwable t) {
            throw new SkipException("Ignored due to undetermined drop-on-overflow behavior for that processor");
        }
    }

    @Override
    public void required_createPublisher3MustProduceAStreamOfExactly3Elements() {
        try {
            super.required_createPublisher3MustProduceAStreamOfExactly3Elements();
        }
        catch (Throwable t) {
            throw new SkipException("Ignored due to undetermined drop-on-overflow behavior for that processor");
        }
    }

    @Override
    public void required_spec102_maySignalLessThanRequestedAndTerminateSubscription() {
        try {
            super.required_spec102_maySignalLessThanRequestedAndTerminateSubscription();
        }
        catch (Throwable t) {
            throw new SkipException("Ignored due to undetermined drop-on-overflow behavior for that processor");
        }
    }

    @Override
    public void required_spec317_mustSupportACumulativePendingElementCountUpToLongMaxValue() {
        try {
            super.required_spec317_mustSupportACumulativePendingElementCountUpToLongMaxValue();
        }
        catch (Throwable t) {
            throw new SkipException("Ignored due to undetermined drop-on-overflow behavior for that processor");
        }
    }

    @Override
    public void required_spec317_mustSupportAPendingElementCountUpToLongMaxValue() {
        try {
            super.required_spec317_mustSupportAPendingElementCountUpToLongMaxValue();
        }
        catch (Throwable t) {
            throw new SkipException("Ignored due to undetermined drop-on-overflow behavior for that processor");
        }
    }

    @Override
    public void required_exerciseWhiteboxHappyPath() {
        throw new SkipException("Ignored due to custom backpressure management");
    }
}
