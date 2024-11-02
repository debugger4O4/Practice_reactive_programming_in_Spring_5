package ru.study.chapter_06._10_memory_consumption_of_different_processing_models;

/**
 * Потребление памяти разными моделями обработки.
 * BlockingDemoApplication - имитация сетевых вызовов и другой активности, связанной с вводом/выводом.
 */
@RestController
@SpringBootApplication
public class BlockingDemoApplication {
    ...
    @GetMapping("/endpoint")
    public String get() throws InterruptedException {
        Thread.sleep(1000);
        return "Hello!";
    }
}

/*
Запуск с настройками java -Xmx2g
                        -Xms1g
                        -Dserver.tomctat.max-threads=20000
                        -Dserver.tomctat.max-connections=20000
                        -Dserver.tomctat.accept-count=20000
                        -jar blocking-demo-0.0.1-SNAPSHOT.jar
не сможет обслуживать 10_000 пользователей - выбросит исключение OutOfMemoryError.
 */
