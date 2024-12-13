/**
 * Spring Cloud Streams как мост в экосистему Spring.
 */
@SpringBootApplication
@EnableBinding(Source.class) // Базовая интеграция с потоковой инфраструктурой.
@EnableConfigurationProperties(...)
public class GitterService implements ChatService<MessageResponse> {

    // Поля и конструкторы ...

    @StreamEmitter // Объявление потока FLux сообщений.
    @Output(Source.OUTPUT) // Определяет имя очереди, в которую должно быть доставлено сообщение.
    // Возвращает бесконечный поток сообщений от Gitter.
    public Flux<MessageResponse> getMessagesStream() { ... }

    @StreamEmitter
    @Output(Source.OUTPUT)
    // Возвращает конечный поток с последними сообщениями из Gitter и посылает их в канал назначения.
    public Flux<MessageResponse> getLatestMessages() { ... }

    public static void main(String[] args) {
        SpringApplication.run(GitterService.class, newArgs);
    }
}