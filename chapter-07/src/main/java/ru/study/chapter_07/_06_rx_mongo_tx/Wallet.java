package ru.study.chapter_07._06_rx_mongo_tx;

/**
 * Реактивные транзакции в MongoDB4.
 */
@Document(collection = "wallet")
public class Wallet {
    @Id
    private ObjectId id;
    private String owner;
    private int balance;

    // Конструкторы и методы свойств опущены...

    // Проверка, достатотчно ли средств.
    public boolean hasEnoughFunds(int amount) {
        return balance >= amount;
    }

    // Списание.
    public void withdraw(int amount) {
        if (!hasEnoughFunds(amount)) {
            throw new RuntimeException("Not enough funds!");
        }
        this.balance = this.balance - amount;
        this.withdrawOperations += 1;
    }

    // Пополнение.
    public void deposit(int amount) {
        this.balance = this.balance + amount;
        this.depositOperations += 1;
    }
}
