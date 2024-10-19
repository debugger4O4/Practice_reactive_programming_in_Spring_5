package ru.study.chapter_04;

import org.junit.Ignore;
import org.junit.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Disposable;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.IntStream;


public class ReactorEssentialsTest {

    private final Random random = new Random();
    Logger log = LoggerFactory.getLogger(ReactorEssentialsTest.class);

    @Test
    @Ignore
    public void endlessStream() {
        Flux.interval(Duration.ofMillis(1))
                .collectList()
                .block();
    }

    /**
     * Реактивные типы Flux и Mono.
     */
    @Test
    @Ignore
    public void endlessStream2() {
        Flux.range(1, 5)
                .repeat()
                .doOnNext(e -> log.info("E: {}", e))
                .take(100)
                .blockLast();
    }

    /**
     * Flux
     */
    @Test
    @Ignore
    public void endlessStreamAndCauseAnError() {
        // range() - создает последовательность целых чисел от 1 до 100.
        Flux.range(1, 100)
                /*
                 repeat() - повторно подписывается на получение реактивного потока после его исчерпания, т.е.
                 подписывается на поток, принимает элементы от 1 до 100 и сигнал onComplete() и затем снова подписывается
                 на поток, опять принимает элементы от 1 до 100 и т.д.
                 */
                .repeat()
                /*
                collectList() - сборка всех собранных лементов в один большой список.
                 */
                .collectList()
                /*
                block() - создает фактическую подписку и блокирует поток выполнения до получения окончательного
                результата, который в данном случае не будет получен, потому что поток данных не имеет конца.
                 */
                .block();
    }

    /**
     * Создание последовательностей Flux и Mono.
     */
    @Test
    public void createFlux() {
        Flux<String> stream1 = Flux.just("Hello", "world");
        Flux<Integer> stream2 = Flux.fromArray(new Integer[]{1, 2, 3});
        Flux<Integer> stream3 = Flux.range(1, 500);

        Flux<String> emptyStream = Flux.empty();
        Flux<String> streamWithError = Flux.error(new RuntimeException("Hi!"));
    }

    @Test
    public void createMono() {
        Mono<String> stream4 = Mono.just("One");
        Mono<String> stream5 = Mono.justOrEmpty(null);
        Mono<String> stream6 = Mono.justOrEmpty(Optional.empty());

        // Обертка долго выполняющегося запроса.
        Mono<String> stream7 = Mono.fromCallable(() -> httpRequest());
        // Предыдущая строчка с использованием синтаксиса Java 8.
        Mono<String> stream8 = Mono.fromCallable(this::httpRequest);

        StepVerifier.create(stream8)
                .expectErrorMessage("IO error")
                .verify();

        Mono<Void> noData = Mono.fromRunnable(() -> doLongAction());

        StepVerifier.create(noData)
                .expectSubscription()
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }

    @Test
    public void emptyOrError() {
        Flux<String> empty = Flux.empty();
        Mono<String> error = Mono.error(new RuntimeException("Unknown id"));
    }

    @Test
    public void managingSubscription() throws InterruptedException {
        // interval генерирует события с заданной периодичностью. Генерируемый поток данных не имеет конца.
        Disposable disposable = Flux.interval(Duration.ofMillis(50))
                // При отмене подписки вывести в лог "Cancelled".
                .doOnCancel(() -> log.info("Cancelled"))
                // Подписка на поток, задается только сигнал onNext.
                .subscribe(
                        data -> log.info("onNext: {}", data)
                );
        // Ждем некоторое время, чтобы получить несколько событий (200 / 50 = 4 события).
        Thread.sleep(200);
        // Отмена подписки.
        disposable.dispose();
    }

    /**
     * Реализация своих подписчиков.
     */
    @Test
    public void subscribingOnStream() throws Exception {
        Subscriber<String> subscriber = new Subscriber<String>() {
            /*
            Подписчик должен хранить ссылку на экземпляр Subscription, связывающий издатиля Publisher и подписчика
            Subscriber. Используется volatile, потому что подписка и обработка данных могут происходить в разных потоках.
            Обеспечивает правильность ссылки на экземпляр во всех потоках выполнения.
             */
            volatile Subscription subscription;

            // Информирование Subscriber об оформлении подписки обратным вызовом.
            @Override
            public void onSubscribe(Subscription s) {
                // Сохранение подписки.
                subscription = s;
                log.info("initial request for 1 element");
                // Посылка запроса с начальными требованиями.
                subscription.request(1);
            }

            // Регистрация полученных данных и запрос следующего элемента.
            public void onNext(String s) {
                log.info("onNext: {}", s);
                log.info("requesting 1 more element");
                // Модель PULL. Управление обратным давлением.
                subscription.request(1);
            }

            public void onComplete() {
                log.info("onComplete");
            }

            public void onError(Throwable t) {
            }
        };

        // Генерация простого потока с помощью just.
        Flux<String> stream = Flux.just("Hello", "world", "!");
        // Подписка.
        stream.subscribe(subscriber);

        Thread.sleep(100);
    }

    @Test
    public void simpleSubscribe() {
        Flux.just("A", "B", "C")
                .subscribe(
                        System.out::println,
                        errorIgnored -> {
                        },
                        () -> System.out.println("Done"));
    }

    @Test
    public void mySubscriber() {
        Flux.just("A", "B", "C")
                .subscribe(new MySubscriber<>());
    }

    @Test
    public void simpleRange() {
        Flux.range(2010, 9)
                .subscribe(y -> System.out.print(y + ","));
    }

    @Test
    public void shouldCreateDefer() {
        Mono<User> userMono = requestUserData(null);
        StepVerifier.create(userMono)
                .expectNextCount(0)
                .expectErrorMessage("Invalid user id")
                .verify();
    }

    @Test
    public void startStopStreamProcessing() throws Exception {
        Mono<?> startCommand = Mono.delay(Duration.ofSeconds(1));
        Mono<?> stopCommand = Mono.delay(Duration.ofSeconds(3));
        Flux<Long> streamOfData = Flux.interval(Duration.ofMillis(100));

        streamOfData
                .skipUntilOther(startCommand)
                .takeUntilOther(stopCommand)
                .subscribe(System.out::println);

        Thread.sleep(4000);
    }

    /**
     * Сбор данных из реактивных последовательностей.
     */
    @Test
    public void collectSort() {
        Flux.just(1, 6, 2, 8, 3, 1, 5, 1)
                .collectSortedList(Comparator.reverseOrder())
                .subscribe(System.out::println);
    }

    /**
     * Отображение элементов реактивных последовательностей.
     */
    @Test
    public void indexElements() {
        // Создание диапазон значений 2018 - 2022.
        Flux.range(2018, 5)
                // Прикрепление текущего времени к каждому значению.
                .timestamp()
                // Прикрепление индекса к каждому значению.
                .index()
                // Подписка. Вывод значений в журнал.
                .subscribe(e -> log.info("index: {}, ts: {}, value: {}",
                        // Индекс.
                        e.getT1(),
                        // Метка времени в удобночитаемом формате.
                        Instant.ofEpochMilli(e.getT2().getT1()),
                        // Фактическое значение.
                        e.getT2().getT2()));
    }

    /**
     * Сокращение элементов потока.
     */
    @Test
    public void findingIfThereIsEvenElements() {
        Flux.just(3, 5, 7, 9, 11, 15, 16, 17)
                // any(Predicate) - проверяет не только значение, но и любое другое его свойство.
                .any(e -> e % 2 == 0)
                .subscribe(hasEvens -> log.info("Has evens: {}", hasEvens));
    }

    @Test
    public void reduceExample() {
        Flux.range(1, 5)
                // scan() - передача промежуточных результатов при агрегировании.
                .scan(0, (acc, elem) -> acc + elem)
                .subscribe(result -> log.info("Result: {}", result));
    }

    @Test
    public void runningAverageExample() {
        // Размер скользящего окна для вычисления среднего значения. Последние 5 элементов.
        int bucketSize = 5;
        // Генерация данных.
        Flux.range(1, 500)
                // Присоединение индекса к каждому элементу.
                .index()
                /*
                 Накопление последних 5ти элементов. Индексы используются для определения позиции в контейнере. На
                 каждом шаге возвращается тот же самый контейнер с обновленными данными.
                 */
                .scan(
                        new int[bucketSize],
                        (acc, elem) -> {
                            acc[(int) (elem.getT1() % bucketSize)] = elem.getT2();
                            return acc;
                        })
                /*
                 Пропуск нескольких элементов с начала потока, чтобы накопить достаточный объем данных для вычислений
                 скользящего среднего.
                 */
                .skip(bucketSize)
                // Получение скользящего среднего.
                .map(array -> Arrays.stream(array).sum() * 1.0 / bucketSize)
                // Подписка, чтобы получать данные.
                .subscribe(av -> log.info("Running average: {}", av));
    }

    /*
     then(), thenMany(), thenEmpty() завершаются с окончанием входящего потока. Игнорируют входящие элементы и
     воспроизводят только сигналы завершения и ошибки.
     */
    @Test
    public void thenOperator() {
        Flux.just(1, 2, 3)
                .thenMany(Flux.just(5, 6))
                // Получится только 4 и 5, даже при том, что 1, 2, 3 были сгенерированы и обработаны потоком.
                .subscribe(e -> log.info("onNext: {}", e));
    }

    /**
     * Комбинирование реактивных потоков.
     */
    @Test
    public void combineLatestOperator() {
        Flux.concat(
                Flux.range(1, 3), // 1 2 3
                Flux.range(4, 2), // 4 5
                Flux.range(6, 5)  // 6 7 8 9 10
        ).subscribe(e -> log.info("onNext: {}", e));
    }

    /**
     * Пакетная обработка эдементов поток.
     */
    //  Буферизация целых чисел по размеру списк 4.
    @Test
    public void bufferBySize() {
        Flux.range(1, 13)
                .buffer(4)
                .subscribe(e -> log.info("onNext: {}", e)); // [1, 2, 3, 4], [5, 6, 7, 8], [9, 10, 11, 12], [13]
    }

    /**
     * Операторы flatMap, concatMap и flatMapSequential.
     */
    // Разбиение последовательности на кадры каждый раз, когда будет встречаться простое число.
    @Test
    public void windowByPredicate() {
        // Генерация последовательности из последних 20ти целых чисел, начиная со 101.
        Flux<Flux<Integer>> fluxFlux = Flux.range(101, 20)
                // Начало нового кадра до предката (Предикат == простое число).
                .windowUntil(this::isPrime, true);

        // Подписка с реактивным преобразованием для каждого подпотока.
        fluxFlux.subscribe(window -> window
                // Преобразование в Mono<List<Integer>>.
                .collectList()
                // Создание отдельной подписки внутри Mono и регистрация полученных событий.
                .subscribe(e -> log.info("window: {}", e))); /*
                                                                [],
                                                                [101, 102],
                                                                [103, 104, 105, 106],
                                                                [107, 108],
                                                                [109, 110, 111, 112],
                                                                [113, 114, 115, 116, 117, 118, 119, 120]
                                                              */
    }

    /*
     Разделение последовательности на четные и нечетные значения и слежение за двумя последними значениями в каждой
     группе.
     */
    @Test
    public void groupByExample() {
        Flux.range(1, 7)
                .groupBy(e -> e % 2 == 0 ? "Even" : "Odd")
                .subscribe(groupFlux -> groupFlux
                        /*
                         Инициализация пустым списком. Каждый элемент из сгруппированнаго потока добавляется в список.
                         Если в списке оказалось больше двух элементов, самые старые элементы удаляются.
                         */
                        .scan(
                                new LinkedList<>(),
                                (list, elem) -> {
                                    if (list.size() > 1) {
                                        list.remove(0);
                                    }
                                    list.add(elem);
                                    return list;
                                })
                        // Удаление пустых контейнеров.
                        .filter(arr -> !arr.isEmpty())
                        .subscribe(data ->
                                log.info("{}: {}",
                                        groupFlux.key(),
                                        data))); /*
                                                    Odd: [1],
                                                    Even: [2],
                                                    Odd: [1, 3],
                                                    Even: [2, 4],
                                                    Odd: [3, 5],
                                                    Even: [4, 6],
                                                    Odd: [5, 7]
                                                  */
    }

    // Запрос у каждого пользователя список его любимых книг.
    private Flux<String> requestBooks(String user) {
        // Генерация набора случайных целочисленных значений.
        return Flux.range(1, random.nextInt(3) + 1)
                // Имитация задержки при обращении к БД.
                .delayElements(Duration.ofMillis(3))
                // Отображение каждого значения в названии книги.
                .map(i -> "book-" + i);
    }

    // Вызов requestBooks() для нескольких пользователей.
    @Test
    public void flatMapExample() throws InterruptedException {
        Flux.just("user-1", "user-2", "user-3")
                /*
                 Преобразование каждого входящего элемента в реактивный поток (T -> Flux<R>), а операция преобразования
                 в плоское представление сливает сгенерированные реактивные последовательности в новую реактивную
                 последовательность, передавая через нее элементы типа R.
                 */
                .flatMap(u -> requestBooks(u)
                        .map(b -> u + "/" + b))
                .subscribe(r -> log.info("onNext: {}", r)); /*
                                                                [thread: parallel-3] onNext: user-3/book-1
                                                                [thread: parallel-1] onNext: user-1/book-1
                                                                [thread: parallel-1] onNext: user-2/book-1
                                                                [thread: parallel-4] onNext: user-3/book-2
                                                                [thread: parallel-5] onNext: user-2/book-2
                                                                [thread: parallel-6] onNext: user-1/book-2
                                                                [thread: parallel-7] onNext: user-3/book-3
                                                                [thread: parallel-8] onNext: user-2/book-3
                                                             */

        Thread.sleep(1000);
    }

    /**
     * Извлечение выборки элементов.
     */
    /*
     sample и sampleTimeout применяется для извлечения выборки. Используятся, например в сценариях, когда требуется
     высокая пропускная способность.
     */
    @Test
    public void sampleExample() throws InterruptedException {
        Flux.range(1, 100)
                .delayElements(Duration.ofMillis(1))
                .sample(Duration.ofMillis(20))
                .subscribe(e -> log.info("onNext: {}", e)); /*
                                                                onNext: 13
                                                                onNext: 28
                                                                onNext: 43
                                                                onNext: 58
                                                                onNext: 73
                                                                onNext: 89
                                                                onNext: 100
                                                             */

        Thread.sleep(1000);
    }

    @Test
    public void doOnExample() {
        Flux.just(1, 2, 3)
                .concatWith(Flux.error(new RuntimeException("Conn error")))
                .doOnEach(s -> log.info("signal: {}", s))
                .subscribe();
    }

    @Test
    public void signalProcessing() {
        Flux.range(1, 3)
                .doOnNext(e -> System.out.println("data  : " + e))
                .materialize()
                .doOnNext(e -> System.out.println("signal: " + e))
                .dematerialize()
                .collectList()
                .subscribe(r -> System.out.println("result: " + r));
    }

    @Test
    public void signalProcessingWithLog() {
        Flux.range(1, 3)
                .log("FluxEvents")
                .subscribe(e -> {}, e -> {}, () -> {}, s -> s.request(2));
    }

    @Test
    public void tryWithResources() {
        try (Connection conn = Connection.newConnection()) {
            conn.getData().forEach(
                    data -> log.info("Received data: {}", data)
            );
        } catch (Exception e) {
            log.info("Error: {}", e.getMessage());
        }
    }

    @Test
    public void usingOperator() {
        Flux<String> ioRequestResults = Flux.using(
                Connection::newConnection,
                connection -> Flux.fromIterable(connection.getData()),
                Connection::close
        );

        ioRequestResults
                .subscribe(
                        data -> log.info("Received data: {}", data),
                        e -> log.info("Error: {}", e.getMessage()),
                        () -> log.info("Stream finished"));
    }

    static class Transaction {
        private static final Random random = new Random();
        private final int id;
        Logger log = LoggerFactory.getLogger(Transaction.class);


        public Transaction(int id) {
            this.id = id;
            log.info("[T: {}] created", id);
        }

        public static Mono<Transaction> beginTransaction() {
            return Mono.defer(() ->
                    Mono.just(new Transaction(random.nextInt(1000))));
        }

        public Flux<String> insertRows(Publisher<String> rows) {
            return Flux.from(rows)
                    .delayElements(Duration.ofMillis(100))
                    .flatMap(row -> {
                        if (random.nextInt(10) < 2) {
                            return Mono.error(new RuntimeException("Error on: " + row));
                        } else {
                            return Mono.just(row);
                        }
                    });
        }


        public Mono<Void> commit() {
            return Mono.defer(() -> {
                log.info("[T: {}] commit", id);
                if (random.nextBoolean()) {
                    return Mono.empty();
                } else {
                    return Mono.error(new RuntimeException("Conflict"));
                }
            });
        }

        public Mono<Void> rollback() {
            return Mono.defer(() -> {
                log.info("[T: {}] rollback", id);
                if (random.nextBoolean()) {
                    return Mono.empty();
                } else {
                    return Mono.error(new RuntimeException("Conn error"));
                }
            });
        }
    }

    @Test
    public void usingWhenExample() throws InterruptedException {
        Flux.usingWhen(
                Transaction.beginTransaction(),
                transaction -> transaction.insertRows(Flux.just("A", "B")),
                Transaction::commit
        ).subscribe(
                d -> log.info("onNext: {}", d),
                e -> log.info("onError: {}", e.getMessage()),
                () -> log.info("onComplete")
        );

        Thread.sleep(1000);
    }

    @Test
    public void usingPushOperator() throws InterruptedException {
        Flux.push(emitter -> IntStream
                        .range(2000, 100000)
                        .forEach(emitter::next))
                .delayElements(Duration.ofMillis(1))
                .subscribe(e -> log.info("onNext: {}", e));

        Thread.sleep(1000);
    }

    @Test
    public void usingCreateOperator() throws InterruptedException {
        Flux.create(emitter -> {
                    emitter.onDispose(() -> log.info("Disposed"));
                    // push events to emitter
                })
                .subscribe(e -> log.info("onNext: {}", e));

        Thread.sleep(1000);
    }

    @Test
    public void usingGenerate() throws InterruptedException {
        Flux.generate(
                        () -> Tuples.of(0L, 1L),
                        (state, sink) -> {
                            log.info("generated value: {}", state.getT2());
                            sink.next(state.getT2());
                            long newValue = state.getT1() + state.getT2();
                            return Tuples.of(state.getT2(), newValue);
                        })
                .take(7)
                .subscribe(e -> log.info("onNext: {}", e));

        Thread.sleep(100);
    }

    @Test
    public void managingDemand() {
        // Генерация 100 значений.
        Flux.range(1, 100)
                // Подписка на поток.
                .subscribe(
                        data -> log.info("onNext: {}", data),
                        err -> { /* ignore */ },
                        () -> log.info("onComplete"),
                        // Управление подпиской.
                        subscription -> {
                            // Запрос 4 элемента.
                            subscription.request(4);
                            // Отмена подписки.
                            subscription.cancel();
                        }
                );
    }

    public Flux<String> recommendedBooks(String userId) {
        return Flux.defer(() -> {
            if (random.nextInt(10) < 7) {
                return Flux.<String>error(new RuntimeException("Conn error"))
                        .delaySequence(Duration.ofMillis(100));
            } else {
                return Flux.just("Blue Mars", "The Expanse")
                        .delayElements(Duration.ofMillis(50));
            }
        }).doOnSubscribe(s -> log.info("Request for {}", userId));
    }

//    @Test
//    public void handlingErrors() throws InterruptedException {
//        Flux.just("user-1")
//                .flatMap(user ->
//                        recommendedBooks(user)
                                  // Невозможно разрешить метод retryBackoff()
//                                .retryBackoff(5, Duration.ofMillis(100))
//                                .timeout(Duration.ofSeconds(3))
//                                .onErrorResume(e -> Flux.just("The Martian"))
//                )
//                .subscribe(
//                        b -> log.info("onNext: {}", b),
//                        e -> log.warn("onError: {}", e.getMessage()),
//                        () -> log.info("onComplete")
//                );
//
//        Thread.sleep(5000);
//    }

    @Test
    public void coldPublisher() {
        Flux<String> coldPublisher = Flux.defer(() -> {
            log.info("Generating new items");
            return Flux.just(UUID.randomUUID().toString());
        });

        log.info("No data was generated so far");
        coldPublisher.subscribe(e -> log.info("onNext: {}", e));
        coldPublisher.subscribe(e -> log.info("onNext: {}", e));
        log.info("Data was generated twice for two subscribers");
    }

    @Test
    public void connectExample() {
        Flux<Integer> source = Flux.range(0, 3)
                .doOnSubscribe(s ->
                        log.info("new subscription for the cold publisher"));

        ConnectableFlux<Integer> conn = source.publish();

        conn.subscribe(e -> log.info("[Subscriber 1] onNext: {}", e));
        conn.subscribe(e -> log.info("[Subscriber 2] onNext: {}", e));

        log.info("all subscribers are ready, connecting");
        conn.connect();
    }

    @Test
    public void cachingExample() throws InterruptedException {
        Flux<Integer> source = Flux.range(0, 2)
                .doOnSubscribe(s ->
                        log.info("new subscription for the cold publisher"));

        Flux<Integer> cachedSource = source.cache(Duration.ofSeconds(1));

        cachedSource.subscribe(e -> log.info("[S 1] onNext: {}", e));
        cachedSource.subscribe(e -> log.info("[S 2] onNext: {}", e));

        Thread.sleep(1200);

        cachedSource.subscribe(e -> log.info("[S 3] onNext: {}", e));
    }

    @Test
    public void replayExample() throws InterruptedException {
        Flux<Integer> source = Flux.range(0, 5)
                .delayElements(Duration.ofMillis(100))
                .doOnSubscribe(s ->
                        log.info("new subscription for the cold publisher"));

        Flux<Integer> cachedSource = source.share();

        cachedSource.subscribe(e -> log.info("[S 1] onNext: {}", e));
        Thread.sleep(400);
        cachedSource.subscribe(e -> log.info("[S 2] onNext: {}", e));

        Thread.sleep(1000);
    }

    @Test
    public void elapsedExample() throws InterruptedException {
        Flux.range(0, 5)
                .delayElements(Duration.ofMillis(100))
                .elapsed()
                .subscribe(e -> log.info("Elapsed {} ms: {}", e.getT1(), e.getT2()));

        Thread.sleep(1000);
    }

    @Test
    public void transformExample() {
        Function<Flux<String>, Flux<String>> logUserInfo =
                stream -> stream
                        .index()
                        .doOnNext(tp ->
                                log.info("[{}] User: {}", tp.getT1(), tp.getT2()))
                        .map(Tuple2::getT2);

        Flux.range(1000, 3)
                .map(i -> "user-" + i)
                .transform(logUserInfo)
                .subscribe(e -> log.info("onNext: {}", e));
    }

//    @Test
//    public void composeExample() {
//        Function<Flux<String>, Flux<String>> logUserInfo = (stream) -> {
//            if (random.nextBoolean()) {
//                return stream
//                        .doOnNext(e -> log.info("[path A] User: {}", e));
//            } else {
//                return stream
//                        .doOnNext(e -> log.info("[path B] User: {}", e));
//            }
//        };
          // Невозможно разрешить метод compose()
//        Flux<String> publisher = Flux.just("1", "2")
//                .compose(logUserInfo);
//
//        publisher.subscribe();
//        publisher.subscribe();
//    }

    public Mono<User> requestUserData(String userId) {
        return Mono.defer(() ->
                isValid(userId)
                        ? Mono.fromCallable(() -> requestUser(userId))
                        : Mono.error(new IllegalArgumentException("Invalid user id")));
    }

    public Mono<User> requestUserData2(String userId) {
        return isValid(userId)
                ? Mono.fromCallable(() -> requestUser(userId))
                : Mono.error(new IllegalArgumentException("Invalid user id"));
    }

    private boolean isValid(String userId) {
        return userId != null;
    }

    private void doLongAction() {
        log.info("Long action");
    }

    private User requestUser(String id) {
        return new User();
    }

    private String httpRequest() {
        log.info("Making HTTP request");
        throw new RuntimeException("IO error");
    }

    public boolean isPrime(int number) {
        return number > 2
                && IntStream.rangeClosed(2, (int) Math.sqrt(number))
                .noneMatch(n -> (number % n == 0));
    }

    static class Connection implements AutoCloseable {
        private final Random rnd = new Random();
        static Logger log = LoggerFactory.getLogger(Connection.class);

        static Connection newConnection() {
            log.info("IO Connection created");
            return new Connection();
        }

        public Iterable<String> getData() {
            if (rnd.nextInt(10) < 3) {
                throw new RuntimeException("Communication error");
            }
            return Arrays.asList("Some", "data");
        }

        @Override
        public void close() {
            log.info("IO Connection closed");
        }
    }

    // Более удачная версия подписчика по сравнению с subscribingOnStream().
    public static class MySubscriber<T> extends BaseSubscriber<T> {

        public void hookOnSubscribe(Subscription subscription) {
            System.out.println("Subscribed");
            request(1);
        }

        public void hookOnNext(T value) {
            System.out.println(value);
            request(1);
        }
    }

    static class User {
        public String id, name;
    }
}
