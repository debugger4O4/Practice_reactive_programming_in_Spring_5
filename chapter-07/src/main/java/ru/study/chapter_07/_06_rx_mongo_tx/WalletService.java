package ru.study.chapter_07._06_rx_mongo_tx;

public interface WalletService {

    // Перевод суммы из кошелька fromOwner в кошелек toOwner. Возвращает TxResult - три возможных исхода операций.
    Mono<TxResult> transferMoney(
            Mono<String> fromOwner,
            Mono<String> toOwner,
            Mono<Integer> amount);

    // Получение данных из всех зарегистрированных кошельков и проверка общего баланса.
    Mono<Statistics> reportAllWallets();

    enum TxResult {
        SUCCESS,
        NOT_ENOUGH_FUNDS,
        TX_CONFLICT
    }

    // Агрегированное состояние всех кошелько в системе.
    class Statistics {
        // Реализация опущена.
    }
}
