package ru.study.chapter_07._10_rx_sync;

public abstract class
    ReactiveCrudRepositoryAdapter
        <T, ID, I extends CrudRepository<T, ID>>
    implements ReactiveCrudRepository<T, ID> {

    /*
    Параллелизм планировщика определяет количество одновременно обслуживаемых запросов, поэтому логично использовать
    то же число, что и для пула соединений.
    Если пул соединений используется и для других целей, количество доступных соединений может быть меньше количества
    доступных потоков выполнения, и некоторые потоки могут заблокироваться в ожидании соединения (rxjava2-jdbc лучше
    обрабатывает такой сценарий).
     */
    // Базовый делегат.
    protected final I delegate;
    // Планировщик.
    protected final Scheduler scheduler;

    // Конструктор...

    // Реактивный метод-обертка для блокирующего метода save().
    @Override
    public <S extends T> Mono<S> save(S entity) {
        // Блокирующий вызов заворачивается в Mono.fromCallable().
        return Mono
                .fromCallable(() -> delegate.save(entity))
                // Передача вызова планировщику.
                .subscribeOn(scheduler);
    }

    // Реактивный адаптер для findById.
    @Override
    public Mono<T> findById(Publisher<ID> id) {
        // Подписка на поток идентификаторов.
        return Mono.from(id)
                .flatMap(actualId ->
                        // Вызов делегата.
                        delegate.findById(actualId)
                                // Возврат Optional, который нужно отобразить в экземпляре Mono.
                                .map(Mono::just)
                                // В случает получения пустого значения возвращает пустой Mono.
                                .orElseGet(Mono::empty))
                .subscribeOn(scheduler);
    }

    /*
     Реактивный адаптер deleteAll. Подписывается на поток сущностей, так как мы не можем отобразить один реактивный
     вызов напрямую в один блокирующий вызов. Например поток сущностей может быть бесконечным => удаление элементов
     никогда не происходит.
     */
    @Override
    public Mono<Void> deleteAll(Publisher<? extends T> entityStream) {
        return Flux.from(entityStream)
                .flatMap(entity -> Mono
                        .fromRunnable(() -> delegate.delete(entity))
                        /*
                         Получение потока выполнения от планировщика, поскольку запрос на удаление может выполняться
                         параллельно.
                         */
                        .subscribeOn(scheduler))
                /*
                 Возвращает выходной поток данных, который завершается после завершения  выходящего потока данных и всех
                 операций удаления.
                 */
                .then();
    }

    // Все остальные методы ReactiveCrudRepository...
}
