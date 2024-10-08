package ru.study.chapter_02.pub_sub_app;

import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

@Component
public class TemperatureSensor {
    // ApplicationEventPublisher - Публкация событий в системе
    private final ApplicationEventPublisher publisher;
    // Random - генератор случайных чисел
    private final Random rnd = new Random();
    // ScheduledExecutorService - процесс генерации, планирующий создание следующего значения со случайной задержкой
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public TemperatureSensor(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    /*
     startProcessing() - вызывается Spring Framework после подготовки компонента и запускает процесс генерации
     последовательности случайных значений температуры
     */
    @PostConstruct
    public void startProcessing() {
        this.executor.schedule(this::probe, 1, SECONDS);
    }

    // probe() - Бизнес-логика
    private void probe() {
        double temperature = 16 + rnd.nextGaussian() * 10;
        publisher.publishEvent(new Temperature(temperature));

        // Планирует следующее чтение после некоторой случайной задержки (0-5 секунд)
        executor.schedule(this::probe, rnd.nextInt(5000), MILLISECONDS);
    }
}

