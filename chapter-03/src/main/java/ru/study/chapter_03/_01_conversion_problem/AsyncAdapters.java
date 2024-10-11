package ru.study.chapter_03._01_conversion_problem;

import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public final class AsyncAdapters {

    // Преобразование ListenableFuture в CompletionStage
    public static <T> CompletionStage<T> toCompletion(ListenableFuture<T> future) {

        // Вызов конструктора без аргументов для обеспечения ручного управления экземпляром CompletionStage
        CompletableFuture<T> completableFuture = new CompletableFuture<>();

        // Интеграция с ListenableFuture
        future.addCallback(completableFuture::complete,
                completableFuture::completeExceptionally);

        return completableFuture;
    }

    // Преобразование CompletionStage в ListenableFuture
    public static <T> ListenableFuture<T> toListenable(CompletionStage<T> stage) {

        /*
         SettableListenableFuture - спеуиализированная реализация ListenableFuture, позволяет вручную передать
         результат выполнения CompletionStage - stage.whenComplete()
         */
        SettableListenableFuture<T> future = new SettableListenableFuture<>();

        stage.whenComplete((v, t) -> {
            if (t == null) {
                future.set(v);
            }
            else {
                future.setException(t);
            }
        });

        return future;
    }
}
