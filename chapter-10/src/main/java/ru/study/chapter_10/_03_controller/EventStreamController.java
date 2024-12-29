/**
 * Конечная точка управления журналированием.
 */

@RestController
public class EventStreamController {

    @GetMapping(path = "/temperature-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Temperature> events() {
         .log("sse.temperature", Level.FINE) // Регистрация температуры.
         .map(this::toDto);
    }

    // Мониторинг потоков в Reactor.
    @GetMapping(path = "/temperature-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Temperature> events() {
        return temperatureSensor.temperatureStream()
                .doOnSubscribe(subs -> activeStreams.incrementAndGet())
                .name("temperature.sse-stream") // Добавление имени для точки мониторинга.
                .metrics() // Регистрация новой метрики в экземпляре MeterRegistry.
}