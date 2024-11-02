package ru.study.chapter_03._05_rxjava_reactivestreams;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;
import ratpack.func.Function;
import ratpack.server.RatpackServer;
import ratpack.sse.ServerSentEvents;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import static ratpack.sse.ServerSentEvents.serverSentEvents;

@SpringBootApplication
@RestController
public class LogServiceApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context =
                SpringApplication.run(LogServiceApplication.class, args);

        LogService logsService = context.getBean(LogService.class);
        FileService fileService = context.getBean(FileService.class);

        File tempFile = File.createTempFile("LogServiceApplication", ".tmp");
        tempFile.deleteOnExit();

        fileService.writeTo(tempFile.getAbsolutePath(), logsService.stream());
         /**
          * Усовершенствования в Ratpack
          * Ratpack — это легковесный фреймворк для разработки веб-приложений
          * <b>
          * Он предоставляет простой и эффективный способ построения высокопроизводительных и масштабируемых веб-приложений
          * <b>
          * Особенности фреймворка:
          * <b>
          * — асинхронный и реактивный подход к обработке запросов;
          * — простота использования;
          * — модульная архитектура;
          * — интеграция с другими современными инструментами и технологиями;
          * — поддержка функций непосредственно в коде приложения
          */
          // Начальное действие сервера и объявление обработчика запроса
        RatpackServer.start(server ->
                server.handlers(chain ->
                        chain.all(ctx -> {
                              // Объявление потока записей для журналирования
                            Publisher<String> logs = logsService.stream();
                              /*
                              Подготовка ServerSentEvents. Используется на этапе отображения для преобразования
                              элемнетов, присылаемых издателем Publisher в представление ServerSentEvents
                               */
                            ServerSentEvents events = serverSentEvents(
                                    logs,
                                    event -> event.id(Objects::toString)
                                            .event("log")
                                            .data(Function.identity())
                            );
                              // Отображение потока данных
                            ctx.render(events);
                        })
                )
        );
    }

    @RequestMapping("/")
    public String root() {
        return "Please go to http://localhost:8080/logs";
    }

    @RequestMapping("/logs")
    public SseEmitter mockLogs() {
        SseEmitter emitter = new SseEmitter();
        Flowable.interval(300, TimeUnit.MILLISECONDS)
                .map(i -> "[" + System.nanoTime() + "] [LogServiceApplication] [Thread " + Thread.currentThread() + "] Some loge here " + i + "\n")
                .subscribe(emitter::send, emitter::completeWithError, emitter::complete);
        return emitter;
    }
}