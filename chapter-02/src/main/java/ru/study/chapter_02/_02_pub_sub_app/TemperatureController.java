package ru.study.chapter_02._02_pub_sub_app;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static java.lang.String.format;

/**
 * Публикация конечной точки SSE
 * SSE(Server-Sent Events) - протокол для асинхронной передачи сообщений от сервера к клиенту
 */
@RestController
public class TemperatureController {
    static final long SSE_SESSION_TIMEOUT = 30 * 60 * 1000L;
    private static final Logger log = LoggerFactory.getLogger(TemperatureController.class);

    // CopyOnWriteArraySet позволяет одновременно изменять список и выполнять его обход
    private final Set<SseEmitter> clients = new CopyOnWriteArraySet<>();

    // Канал связи с клиентами
    @RequestMapping(value = "/temperature-stream", method = RequestMethod.GET)
    // Реализация единственного обработчика запросов
    public SseEmitter events(HttpServletRequest request) {
        log.info("SSE stream opened for client: " + request.getRemoteAddr());
        SseEmitter emitter = new SseEmitter(SSE_SESSION_TIMEOUT);
        // Предварительная регистрация нового SseEmitter в списке клиентов
        clients.add(emitter);

        // Удалить SseEmitter из активных клиентов при ошибке или отключении клиента
        emitter.onTimeout(() -> clients.remove(emitter));
        emitter.onCompletion(() -> clients.remove(emitter));

        return emitter;
    }

    // Получение событий со значением температуры
    /*
     @Async - сообщает, что метод может выполняться асинхронно. В данном случае он вызывается из пула потоков,
     сконструированного вручную
     */
    @Async
    // @EventListener - обработчик событий Spring
    @EventListener
    public void handleMessage(Temperature temperature) {
        log.info(format("Temperature: %4.2f C, active subscribers: %d",
                temperature.getValue(), clients.size()));

        List<SseEmitter> deadEmitters = new ArrayList<>();
        clients.forEach(emitter -> {
            try {
                Instant start = Instant.now();
                /*
                 handleMessage() принимает новое событие со значением температуры и асинхронно рассылает его всем
                 клиентам в формате JSON
                 */
                emitter.send(temperature, MediaType.APPLICATION_JSON);
                log.info("Sent to client, took: {}", Duration.between(start, Instant.now()));
            } catch (Exception ignore) {
                // Определение неудачных попыток
                deadEmitters.add(emitter);
            }
        });
        // Удаление из активных клиентов
        clients.removeAll(deadEmitters);
    }

    @ExceptionHandler(value = AsyncRequestTimeoutException.class)
    public ModelAndView handleTimeout(HttpServletResponse rsp) throws IOException {
        if (!rsp.isCommitted()) {
            rsp.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
        return new ModelAndView();
    }
}
