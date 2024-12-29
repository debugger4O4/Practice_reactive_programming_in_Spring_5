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
}