package ru.study.chapter_07._06_rx_mongo_tx;

/**
 * Использование реактивной поддержки транзакций.
 */
public class TransactionalWalletService extends WalletService {
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<TxResult> transferMoney(
            Mono<String> fromOwner,
            Mono<String> toOwner,
            Mono<Integer> requestAmount
    ) {
        // Подписка на все аргументы с помощью zip().
        return Mono.zip(fromOwner, toOwner, requestAmount)
                /*
                 function() - разложение Tuple3<String, String, Integer> на составляющие для последующего использования.

                 */
                .flatMap(function((from, to, amount) -> {
                    Instant start = now();
                    // Перевод денег.
                    return doTransferMoney(from, to, amount)
                            // Повторное выполнение транзакции в случае ошибки.
                            .retryBackoff(
                                    /*
                                    20 - число попыток,
                                    Duration.ofMillis(1) - начальная задержка,
                                    Duration.ofMillis(50) - максимальная задержка,
                                    0.1 - величина флуктуаций - случайные изменения, колебания или вариации от среднего
                                    значения в какой-либо системе.
                                     */
                                    20, Duration.ofMillis(1),
                                    Duration.ofMillis(50), 0.1
                            )
                            // Возвращает TX_CONFLICT, если после всех повторных попыток отправка не удалась.
                            .onErrorReturn(TxResult.TX_CONFLICT)
                            .doOnSuccess(result -> log.info("Transaction result: {}, took: {}",
                                    result, Duration.between(start, now())));
                }));
    }

    // Попытка фактического перевода денег.
    private Mono<TxResult> doTransferMoney(
            String from,
            String to,
            Integer amount
    ) {
        // mongoTemplate.inTransaction().execute(...) - определение границы новой транзакции.
        return mongoTemplate.inTransaction().execute(session ->
                        session
                                // Поиск кошелька отправителя.
                                .findOne(queryForOwner(from), Wallet.class)
                                .flatMap(fromWallet -> session
                                        // Кошелек получателя.
                                        .findOne(queryForOwner(to), Wallet.class)
                                        .flatMap(toWallet -> {
                                            // Проверка достаточно ли средств на счету отправителя.
                                            if (fromWallet.hasEnoughFunds(amount)) {
                                                // Списание требуемой суммы со счета отправителя.
                                                fromWallet.withdraw(amount);
                                                // Внесение суммы на счет получателя.
                                                toWallet.deposit(amount);

                                                // Сохранение изменений в кошельке отправителя.
                                                return session.save(fromWallet)
                                                        // Сохранение изменений в кошельке отправителя.
                                                        .then(session.save(toWallet))
                                                        .then(ReactiveMongoContext.getSession())
                                                        .doOnNext(tx -> log.info("Current session: {}", tx))
                                                        // SUCCESS - БД не отвергла изменения.
                                                        .then(Mono.just(TxResult.SUCCESS));
                                            } else {
                                                // NOT_ENOUGH_FUNDS - недостаточно средств.
                                                return Mono.just(TxResult.NOT_ENOUGH_FUNDS);
                                            }
                                        })))
                // Посылка сигнала об ошибке взаимодействия с БД.
                .onErrorResume(e -> Mono.error(new RuntimeException("Conflict")))
                .last();
    }

    // Использование CriteriaApi для конструирования запросов к БД MongoDB.
    private Query queryForOwner(String owner) {
        return Query.query(new Criteria("owner").is(owner));
    }
}
