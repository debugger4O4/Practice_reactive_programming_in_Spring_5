/**
 * Реализация своих параметров Micrometer.
 */

public class Micrometer {

    WebClient.create(serviceUri) // Создание нового WebClinet.
            .get()
            .exchange()
            .fltaMap(cr -> cr.toEntity(User.class)) // Преобразование WebClient в User.
            .doOnTerminate(() -> registry
                .counter("user.request", "uri", serviceUri)
            .increment()) // Увеличение счетчика.
}