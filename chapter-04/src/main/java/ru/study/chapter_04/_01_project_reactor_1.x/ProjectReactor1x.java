package ru.study.chapter_04._01_project_reactor_1.x;


/**
 * Project Reactor 1.x.
 */
public class ProjectReactor1x {
    /*
    Environment определяет контекст выполнения, который используется при создании экземпляра Dispatcher. Может возвращать
    разне типы диспетчеров - от действующих внутри процесса до распределенных.
     */
    Environment env = new Environment();
    // Reactor - прямая реализация Reactor Pattern
    Reactor reactor = Reactors.reactor()
            .env(env)
              /*
               Переопределенная реализация Dispatcher, основанная на структуре RingBuffer.
               Реализация Dispatcher, основанного на структуре Ring Buffer, представляет собой способ организации
               очереди сообщений для управления потоками выполнения задач. В этом подходе используется структура
               данных, которая позволяет эффективно обрабатывать сообщения без необходимости использования сложных
               механизмов синхронизации.
               Основные компоненты реализации:
               1. RingBuffer - это структура данных, которая хранит сообщения и обеспечивает эффективный доступ к ним.
               RingBuffer состоит из массива элементов и двух указателей: `head` и `tail`. Указатель `head` указывает
               на текущий элемент, который будет обработан следующим, а указатель `tail` указывает на следующий
               свободный элемент. Когда все элементы будут обработаны, указатели циклически перемещаются к началу буфера.
               2. Dispatcher - этот класс отвечает за управление сообщениями в кольцевом буфере. Он предоставляет
               методы для добавления новых сообщений, получения следующего сообщения для обработки и очистки буфера.
               Dispatcher также может управлять приоритетами сообщений, чтобы гарантировать своевременную обработку
               важных событий.
               */
            .dispatcher(Environment.RING_BUFFER)
            .get();
      /*
      Объявление селектора кнала и потребителя событий. Фильтрация событий производится с помощью строкового селектора,
      который определяет имя канала события. $ предлагают широкий выбор кретериев, вследствие чего окончательное
      выражение селектора может оказаться очень сложным.
       */
    reactor.on($("channel"),
      event -> System.out.println(event.getData()));

      /*
      Настройка прозводителей Event в форме планируемого задания, для чего используются возможности
      ScheduledExecutorService. В результате получается задание, выполняющееся периодически и посылающее события Event
      в конкретный канал, созданный экземпляром Reactor.
       */
    Executors.newSingleThreadScheduledExecutor()
            .scheduleAtFixedRate(
                    () -> reactor.notify("channel", Event.wrap("test")),
            0, 100, TimeUnit.MILLISECONDS
    );

//  ==================================================================================================================
      // Создание экземпляров Environment и Reactor
//    ...

    Stream<String> stream = Streams.on(reactor, $("channel"));
    stream.map(s -> "Hello world " + s)
            .distinct()
            .filter((Predicate<String>) s -> s.length() > 2)
            .consume(System.out::println);
      /*
      Создание отложенного потока данных.
      Deferred - специальная обертка, которая позволяет передавать в поток Stream события, созданные вручную.
      Streams.defer(env) создает дополнительный экземпляр класса Reactor.
       */
    Deferred<String, Stream<String>> input = Streams.defer(env);
      /*
      compose() - звлечение Stream из экземпляра Deferred
       */
    Stream<String> compose = input.compose();
    compose.map(m -> m + " Hello World")
            .filter(m -> m.contains("1"))
            .map(Event::wrap)
            .consume(reactor.prepare("channel"));
      // Генерация случайного элемента и передача его в экземпляр Deferred
    for (int i = 0; i < 1000; i++) {
        input.accept(UUID.randomUUID().toString());
    }
}
