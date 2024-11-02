package ru.study.chapter_05._03_reactive_type_conversion_support;

import org.springframework.core.ReactiveAdapterRegistry;

/**
 * Кастомная реализация ReactiveAdapter.
 */
public class MaybeReactiveAdapter extends ReactiveAdapter {
    public MaybeReactiveAdapter() {
    // Вызов родительского конструктора.
        super(
        /*
         Определение экземпляра. ReactiveTypeDescriptor - хранит информацию о реактивном типе, использованном в
         ReactiveAdapter.
         */
            ReactiveTypeDescriptor
                    .singleOptionalValue(Maybe.class, Maybe::empty),
            // Преобразование исходного объекта в Publisher и обратно.
            rawMaybe -> ((Maybe<?> rawMaybe).toFlowable(),
            publisher -> Flowable.fromPublisher(publisher)
                    .singleElement();
        );
    }

    // ReactiveAdapterRegistry - хранит экземпляры ReactiveAdapter в одном месте и обобщающий доступ к ним.
    ReactiveAdapterRegistry
            /*
             Создание только одного экземпляра каждого адаптера, который может использоваться во многих местах внутри
             фреймворка или разрабатываемого приложения.
             */
            .getSharedInstance()
            // Регистрация адаптера.
            .registerReactiveType(
                ReactiveTypeDescriptor
                        .singleOptionValue(Maybe.class, Maybe::empty),
                rawMaybe -> ((Maybe<?> rawMaybe).toFlowable(),
                publisher -> Flowable.fromPublisher(publisher)
                        .singleElement();
    );
    ...
    ReactiveAdapter reactiveAdapter = ReactiveAdapterRegistry
            .getSharedInstance();
            // Получение имеющегося адаптера.
            .getAdapter(Maybe.class);
}
