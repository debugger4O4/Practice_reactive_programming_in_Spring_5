package ru.study.chapter_03.conversion_problem;

import java.util.concurrent.CompletionStage;

/**
 * Проблема несовместимости API
 * <p>
 * AsyncDatabaseClient - интерфейс асинхронного клиента базы данных, который является типичным примером возможного
 * интерфейса клиента с асинхронным доступом к базе данных
 */
public interface AsyncDatabaseClient {

    <T> CompletionStage<T> store(CompletionStage<T> stage);
}
