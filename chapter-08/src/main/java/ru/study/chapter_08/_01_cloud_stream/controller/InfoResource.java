@RestController
@RequestMapping("/api/v1/info")
@EnableBinding({MessagesSource.class, StatisticSource.class})
@SpringBootApplication
public class InfoResource {

    // ...

    @StreamListener
    public void listen(
        @Input(MessagesSource.INPUT) Flux<MessageResponse> messages,
	    @Input(StatisticSource.INPUT) Flux<UsersStatisticVM> statistic
    ) {
        // Подписка на потоки статистики и сообщений.
        messages.map(MessageMapper::toViewModelUnit)
                .subscribeWith(messagesStream);
	    statistic.subscribeWith(statisticStream);
    }

    // ...

	public static void main(String[] args) {
        SpringApplication.run(InfoResource.class, args);
	}
}