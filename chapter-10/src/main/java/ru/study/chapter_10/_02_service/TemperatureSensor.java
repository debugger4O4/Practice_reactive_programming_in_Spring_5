/**
 * Конечная точка для получения информации о работоспособности.
 */

class TemperatureSensor {

    public Mono<Integer> batteryLevel() {
        // Выполнение сетевого запроса к датчику и, если он доступен, возвращает уровень заряда аккумулятора в %.
    }
}