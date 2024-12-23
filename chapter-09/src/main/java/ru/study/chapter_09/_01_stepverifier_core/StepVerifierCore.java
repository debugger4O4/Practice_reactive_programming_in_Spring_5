/**
 * Основы StepVerifier.
 */

public class StepVerifierCore {

    /*
    Проверка издателя Publisher.
    Происходит создание двух конркретных элементов. Последующие операции проверяют, были ли эти элементы доставлены
    подписчику.
     */
    StepVerifier
            .create(Flux.just("foo", "bar"))
            .expectSubscription()
            .expectNext("foo")
            .expectNext("bar")
            .expectComplete()
            .verify(); // Сигнал завершения.

    // Проверка, сгенрировал ли издатель конкретное количество элемнтов.
    StepVerifier
            .create(Flux.ranfe(0, 100))
            .expectSubscription()
        // Проверка посылки элементов в правильном порядке.
            .expectNext(0)
            .expectNext(98)
            .expectNextCount(99)
            .expectComplete()
            .verify();

    // Фильтрация или выбор элементов по определенным правилам.
    Publisher<Wallet> usersWallets = findAllUsersWallets();
    StepVerifier
            .create(usersWallets)
            .expectSubscription()
            .recordWith(ArrayList::new)
            .expectNextCount(1)
            .consumeRecordedWith(wallets -> assertThat(
                    wallets,
                    everyItem(hasProperty("owner", equalTo("admin")))
            ))
            .expectComplete()
            .verify();

    // Прием и проверка элемента.
    StepVerifier
            .create(Flux.just("alpha-foo", "betta-bar"))
            .expectSubscription()
            .expectNextMatches(e -> e.startsWith("alpha"))
            .expectNextMatches(e -> e.startsWith("betta"))
            .expectComplete()
            .verify()

    // Перехват ошибок, сгенерированных потребителем.
    StepVerifier
            .create(findUsersUSDWallet())
            .expectSubscription()
            .assertNext(wallet -> assertThat(
                wallet,
                hasProperty("currency", equalTo("USD"))
            ))
            .expectComplete()
            .verify();

    // Охват ошибочных ситуаций, которые также являются частью нормального жизненного цикла системы.
    StepVerifier
            .create(Flux.error(new RuntimeException("Error")))
            .expectError()
            .verify();

    // Определение конкретного типа ошибки.
    StepVerifier
            .create(securityService.login("admin", "wrong"))
            .expectSubscription()
            .expectError(BadCredentialsException.class)
            .verify();
}