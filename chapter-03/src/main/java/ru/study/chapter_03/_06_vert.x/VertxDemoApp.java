package ru.study.chapter_03._06_vert.x;

import io.reactivex.rxjava3.core.Flowable;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.streams.Pump;

/**
 * Изменения в Vert.x
 * Vert.x - это многоязыковой асинхронный веб-фреймворк, который работает на событийно-ориентированной архитектуре и
 * запускается поверх JVM. Он был разработан Tim Fox в 2011 году, когда он работал в VMware. Vert.x предоставляет свой
 * API на различных языках программирования, включая Java, JavaScript, Groovy, Ruby, Python, Scala, Clojure и Ceylon.
 * С версией 3.7.0, Vert.x поддерживает Kotlin, а с версии 3.9.1 - Java, JavaScript, Groovy, Ruby, Scala and Kotlin.
 * В январе 2016 года Tim Fox покинул проект Vert.x, и его заменил Julien Viet. Vert.x используется в различных
 * проектах, таких как разработка приложений для Интернета вещей (IoT) и корпоративных сервисов
 */
public class VertxDemoApp extends AbstractVerticle {

    @Override
    public void start() {
        LogService logsService = new MockLogService();
        vertx.createHttpServer()
                  /*
                   Объявление обработчика запросов. Обобщенный обработчик, способен обрабатывать любые запросы,
                   отправленные серверу
                   */
                .requestHandler(request -> {
                      /*
                      Объявление подписчика Subscriber и HTTP- ответа. ReactiveReadStream реализует оба интерфейса -
                      - org.reactivestreams.Subscriber и ReadStreams, что позволяет преобразовать любую реализацию
                      Publisher в источник данных, совместимый с Vert.x API
                       */
                    ReactiveReadStream<Buffer> rrs = ReactiveReadStream.readStream();
                    HttpServerResponse response = request.response();
                      /*
                      Объявление потока обработки. Тут идет ссылка на новый интерфейс LogService, основанный на Reactive
                      Streams, и для преобразования элементов из потока данных используем Flowable API и RxJava 2.x.
                       */
                    Flowable<Buffer> logs = Flowable.fromPublisher(logsService.stream())
                            .map(Buffer::buffer)
                            .doOnTerminate(response::end);
                      /*
                      Этап формирования подписки. После объявления потока обработки можно подписать ReactiveReadStream
                      на Flowable
                       */
                    logs.subscribe(rrs);
                      // Этап подготовки ответа
                    response.setStatusCode(200);
                    response.setChunked(true);
                    response.putHeader("Content-Type", "text/plain");
                    response.putHeader("Connection", "keep-alive");
                      /*
                      Отправка окончательного ответа клиенту. Pump играет важную роль в сложном механизме управления
                      обратным давлением для предотвращения переполнения буфера WriteStream
                      Pump — это класс в Java, который используется для передачи данных из ReadStream в WriteStream и
                      выполняет контроль потока, чтобы предотвратить переполнение буфера WriteStream. Instances этого
                      класса читают элементы из ReadStream и записывают их в WriteStream. Если данные могут быть
                      прочитаны быстрее, чем записаны, это может привести к переполнению очереди WriteStream.
                      Чтобы предотвратить это, после каждой записи экземпляры этого класса проверяют, полна ли очередь
                      WriteStream. Если да, то ReadStream приостанавливается, и на WriteStream устанавливается
                      drainHandler. Когда WriteStream обработает половину своего объёма, drainHandler будет вызван,
                      и насос возобновит работу с ReadStream. Этот класс можно использовать для передачи данных из
                      любого ReadStream в любой WriteStream, например, из HttpServerRequest в AsyncFile или из
                      NetSocket в WebSocket
                       */
                    Pump.pump(rrs, response)
                            .start();
                })
                .listen(8080);
        System.out.println("HTTP server started on port 8080");
    }

    public static void main(final String[] args) {
        Launcher.executeCommand("run", VertxDemoApp.class.getName());
    }
}
