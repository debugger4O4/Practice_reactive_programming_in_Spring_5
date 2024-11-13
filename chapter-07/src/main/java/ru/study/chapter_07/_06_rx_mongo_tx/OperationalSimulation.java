package ru.study.chapter_07._06_rx_mongo_tx;

@Builder
@RequiredArgsConstructor
public class OperationalSimulation {

    public Mono<OperationStats> runSimulation() {
        // Имитация желаемого диапазона перевода ДС.
        return Flux.range(0, iterations)
                .flatMap(i -> Mono
                        // Задержка. Имитация случайного конкурентного выполнения транзакции.
                        .delay(Duration.ofMillis(rnd.nextInt(10)))
                        // Определение уровня параллелелизма количеством выполняющихся транзакции.
                        .publishOn(simulationScheduler)
                        .flatMap(_i -> {
                            // Случайный выбор сумм и кошельков для перевода.
                            String fromOwner = randomOwner();
                            String toOwner = randomOwnerExcept(fromOwner);
                            int amount = randomTransferAmount();

                            // Запрос к службе.
                            return walletService.transferMoney(
                                    Mono.just(fromOwner),
                                    Mono.just(toOwner),
                                    Mono.just(amount));
                        }))
                // Отделение статистики моделирования, т.к. transferMoney может привести к одному из состояний TxResult.
                .reduce(OperationStats.start(), OperationStats::countTxResult);
    }
}
