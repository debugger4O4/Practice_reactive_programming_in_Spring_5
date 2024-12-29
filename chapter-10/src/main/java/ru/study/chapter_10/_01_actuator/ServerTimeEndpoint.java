/**
 * Реализация своей конечной точки для Actuator.
 */

@Component
@Endpoint(id = "server-time") // Конечная точка REST(actuator/server-time).
public class ServerTimeEndpoint {

    private Mono<Long> getNtpTimeOffset() {
        // Фактический сетевой вызов для получения смещения текущего времени.
    }

    @ReadOperation
    public Mono<Map<String, Object>> reportServerTime() {
        return getNtpTimeOffset() // Вызов сервера NTP(Популярный протокол для синхронизации часов компьютера по сети).
                .map(timeOffset -> {
                    Map<String, Object> rsp = new LinkedHashMap<>();
                    rsp.put("serverTime", Instant.now().toString());
                    rsp.put("ntpOffsetMillis", timeOffset);
                    return rsp;
                });
    }
}