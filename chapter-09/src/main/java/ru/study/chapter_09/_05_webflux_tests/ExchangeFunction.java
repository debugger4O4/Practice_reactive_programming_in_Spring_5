/**
 * Имитация ответа на исходящий HTTP-вызов.
 */

public interface ExchangeFunction {
    Mono<ClientResponse> exchange(ClientRequest request);
    ...
}