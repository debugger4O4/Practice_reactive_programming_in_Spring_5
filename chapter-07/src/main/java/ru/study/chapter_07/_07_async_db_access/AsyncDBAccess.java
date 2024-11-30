package ru.study.chapter_07._07_async_db_access;

/**
 * Асинхронный доступ к БД.
 */
public class AsyncDBAccess {

    // Запрос возвращает список имен сотрудников.
    CompletionStage<List<String>> employeeNames =
            connection
                    // Вызов rowOperation соединение connection с БД создаеся новая операция.
                    .<Integer>rowOperation("select * from employees")
                    // Вызов обработчика ошибок.
                    .onError(this::userDefinedErrorHandler)
                    // Сбор результата с помощью Java Stream API.
                    .collect(Collector.of(
                            () -> new ArrayList<String>(),
                            (ArrayList<String> cont, Result.RowColumn row) ->
                                    cont = cont.add(row.at("name").get(String.class)),
                            (l, r) -> l,
                            container -> container))
                    // Запуск операции.
                    .submit()
                    /*
                     Возврат пользователю экземпляр CompletionStage, который будет хранить результаты по завершении
                     обработки запроса.
                     */
                    .getCompletionStage();
                    ))
}
