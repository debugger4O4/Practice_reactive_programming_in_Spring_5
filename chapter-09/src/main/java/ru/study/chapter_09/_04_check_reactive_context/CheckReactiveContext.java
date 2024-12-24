/**
 * Проверка реактивного контекста.
 */

public class CheckReactiveContext {

    // Проверка контекста из Reactor.
    StepVerifier
            .create(securityService.login("admin", "admin"))
            .expectSubscription()
            .expectAccessibleContext() /*
                                         Выбросит исключение, если возвращаемый издатель Publicher не являеся типом
                                         Reactor(Flux || Mono).
                                        */
            .hasKey("security") // Проверка текущего контекста.
            .then()
            .expectComplete()
            .verify();
}