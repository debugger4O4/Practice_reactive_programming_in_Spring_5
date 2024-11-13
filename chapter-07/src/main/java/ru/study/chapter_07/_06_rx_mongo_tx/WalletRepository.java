package ru.study.chapter_07._06_rx_mongo_tx;

/**
 * Хранилище.
 */
@Repository
public interface WalletRepository
        extends ReactiveMongoRepository<Wallet, ObjectId> {
    // Получение кошелька по имени владельца.
    Mono<Wallet> findByOwner(Mono<String> owner);
}
