package ru.study.chapter_02._02_pub_sub_app;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Настройка поддержки асинхронного выполнения
 */
@EnableAsync
@SpringBootApplication
public class Application implements AsyncConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // Executor для асинхронной обработки
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("sse-");
        // Количество основных потоков
        executor.setCorePoolSize(2);
        // Маскимальное количество потоков
        executor.setMaxPoolSize(100);
        // Емкость очереди потоков
        executor.setQueueCapacity(5);
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        // Обработчик исключений в процессе асинхронного выполнения
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}
