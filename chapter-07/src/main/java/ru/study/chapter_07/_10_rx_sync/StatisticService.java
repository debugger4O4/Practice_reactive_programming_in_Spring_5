package ru.study.chapter_07._10_rx_sync;


/**
 * Реактивный Spring Data в действии.
 */
public class StatisticService {
    private static final UserVM EMPTY_USER = new UserVM("", "");

    // Ссылки для обеспечения реактивной связи.
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    // Конструктор...

    // Потоковая передача статистических событий.
    // Принимает входящий поток сообщений чата messagesFlux.
    public Flux<UsersStatisticVM> updateStatistic(Flux<MessageResponse> messagesFlux) {
        return messagesFlux
                // Подписка на поток объектов ChatMessage и преобразование их в требуемое представление.
                .map(MessageMapper::toDomainUnit)
                // Сохранение в БД.
                .transform(messageRepository::saveAll)
                // Помощь в преодолении возможных перебоев связи с MongoDB.
                .retryBackoff(Long.MAX_VALUE, Duration.ofMillis(500))
                // Отбрасывание старых сообщений, если подписчик не может обработать все события.
                .onBackpressureLatest()
                // Пересчет статистики.
                .concatMap(e -> this.doGetUserStatistic(), 1)
                // Игнорирование всех ошибок.
                .onErrorContinue((t, e) -> {});
    }

    // Определение самых популярных пользователей.
    private Mono<UsersStatisticVM> doGetUserStatistic() {
        Mono<UserVM> topActiveUserMono = userRepository
                .findMostActive()
                // Преобразование результата в правильный тип.
                .map(UserMapper::toViewModelUnits)
                // Если пользак не найдет, то вернуть предопределенное значение.
                .defaultIfEmpty(EMPTY_USER);

        Mono<UserVM> topMentionedUserMono = userRepository
                // Получение самого популярного пользователя.
                .findMostPopular()
                // Преобразование.
                .map(UserMapper::toViewModelUnits)
                // Предопределенное значение в случае ненахода.
                .defaultIfEmpty(EMPTY_USER);

        // Объединение двух реактивных запросов и создание нового экземпляра UsersStatisticVM.
        return Mono.zip(
                topActiveUserMono,
                topMentionedUserMono,
                UsersStatisticVM::new
                // Установка максимального времени ожидания для пересчета статистики.
                ).timeout(Duration.ofSeconds(2));
    }
}
