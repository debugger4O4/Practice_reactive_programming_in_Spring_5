/**
 * Фиктивная реализация ExchangeFunction.
 */

@TestConfiguration
public class TestWebClientBuilderConfiguration {

    @Bean
    public WebClientCustomizer testWebClientCustomizer(
            ExchangeFunction exchangeFunction
    ) {
        return builder -> builder.exchangeFunction(exchangeFunction);
    }
}