package ru.study.chapter_06._10_memory_consumption_of_different_processing_models;

/**
 * TomcatNoBlockingDemoApplication - моделировагние неблокрующего и асинхронного ввода/вывода.
 */
//@RestController
//@SpringBoootApplication
//public class TomcatNoBlockingDemoApplication {
//    ...
//    @GetMpping("/endpoint")
//    public Mono<String> get() {
//        return Monon.just("Hello!")
//                .delaySubscription(Duration.ofSeconds(1));
//    }
//}

/*
Запуск с настройками java -Xmx2g
                        -Xms1g
                        -Dserver.tomctat.accept-count=20000
                        -jar blocking-demo-0.0.1-SNAPSHOT.jar
сможет обслуживать около 10_000 пользователей со средней задержкой 9_661мс,
с использованием Reactor-Netty - 2_699мс.
 */
