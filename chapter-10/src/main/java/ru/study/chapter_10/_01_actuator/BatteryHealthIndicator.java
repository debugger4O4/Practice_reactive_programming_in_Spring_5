/**
 * Класс работоспособности аккумулятора.
 */

class BatteryHealtIndicator implements ReactiveHealthIndicator {
    private final TemperatureSensor temperatureSensor;

    @Override
    public Mono<Health> health() {
        return temperatureSensor
                .batteryLevel()
                .map(level -> {
                    if (level > 40) {
                        return new Health.Builder()
                                .up()
                                .withDetail("level", level)
                                .build();
                    } else {
                        return new Health.Builder()
                                .status(new Status("Low Battery"))
                                .withDetail("level", level)
                                .build();
                    }
                }).onErrorResume(err -> Mono.
                        just(new Health.Builder()
                                .outOfService()
                                .withDetail("error", err.getMessage())
                                .build())
                );
    }
}