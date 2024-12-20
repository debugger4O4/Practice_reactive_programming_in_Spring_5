/**
 * Реактивный клиент.
 */


public interface TestClient {

    @RequestManyMapping(
            value = "/stream1",
            mimeType = "application/json"
    )
    public Flux<String> stream1(@Payload String a);

    @RequestManyMapping(
            value = "/stream2",
            mimeType = "application/json"
    )
    public Flux<String> stream2(@Payload String b);
}