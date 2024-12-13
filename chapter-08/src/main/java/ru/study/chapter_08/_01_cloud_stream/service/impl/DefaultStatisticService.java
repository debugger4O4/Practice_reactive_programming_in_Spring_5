@SpringBootApplication
@EnableBinding(Processor.class)
public class DefaultStatisticService implements StatisticService {

    // ...

    @StreamListener // Объявление метода процессора.
    @Output(Processor.OUTPUT)
    // Принимает поток Flux, который обеспечивает доступ к входящему объекту из канала брокера сообщений
    public Flux<UsersStatisticVM> updateStatistic(@Input(Processor.INPUT) Flux<MessageResponse> messagesFlux) { ... }

    // ...

    public static void main(String[] args) {
        SpringApplication.run(DefaultStatisticService.class, args);
    }
}