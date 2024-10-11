package ru.study.chapter_02._04_rx_app;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import rx.Subscriber;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Публикация конечной точки SSE
 */
@RestController
public class TemperatureController {
    private static final Logger log = LoggerFactory.getLogger(TemperatureController.class);

    private final TemperatureSensor temperatureSensor;

    public TemperatureController(TemperatureSensor temperatureSensor) {
        this.temperatureSensor = temperatureSensor;
    }

    @RequestMapping(value = "/temperature-stream", method = RequestMethod.GET)
    public SseEmitter events(HttpServletRequest request) {
        /*
        При реактивной реализации контроллер не должен забоиться об уничтожении ненужных экземпляров SseEmitter и о
        синхронизации. Реактивная реализация сама управляет получением TemperatureSensor и их публикацией. Эта реализация
        не использует шину событий из Spring, поэтому она более переносима и может тестироваться без инициализации контекста
         */
        RxSeeEmitter emitter = new RxSeeEmitter();
        log.info("[{}] Rx SSE stream opened for client: {}",
                emitter.getSessionId(), request.getRemoteAddr());

        temperatureSensor.temperatureStream()
                .subscribe(emitter.getSubscriber());

        return emitter;
    }

    @ExceptionHandler(value = AsyncRequestTimeoutException.class)
    public ModelAndView handleTimeout(HttpServletResponse rsp) throws IOException {
        if (!rsp.isCommitted()) {
            rsp.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
        return new ModelAndView();
    }

    /**
     * Нестандартный SseEmitter
     */
    static class RxSeeEmitter extends SseEmitter {
        static final long SSE_SESSION_TIMEOUT = 30 * 60 * 1000L;
        private final static AtomicInteger sessionIdSequence = new AtomicInteger(0);

        private final int sessionId = sessionIdSequence.incrementAndGet();
        // Инкапсуляция реализации подписчика на события Temperature
        private final Subscriber<Temperature> subscriber;

        RxSeeEmitter() {
            // Вызов конструктора SseEmitter и установка тайм-аута сеанса
            super(SSE_SESSION_TIMEOUT);

            // Создание экземпляра класса
            this.subscriber = new Subscriber<Temperature>() {
                // Экземпляр реагирует на сигналы onNext() и пересылает их клиенту SSE
                @Override
                public void onNext(Temperature temperature) {
                    try {
                        RxSeeEmitter.this.send(temperature);
                        log.info("[{}] << {} ", sessionId, temperature.getValue());
                    } catch (IOException e) {
                        log.warn("[{}] Can not send event to SSE, closing subscription, message: {}",
                                sessionId, e.getMessage());
                        // Если отправка данных не удалась, подписчик отменяет подписку на входящий поток Observable
                        unsubscribe();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    log.warn("[{}] Received sensor error: {}", sessionId, e.getMessage());
                }

                @Override
                public void onCompleted() {
                    log.warn("[{}] Stream completed", sessionId);
                }
            };

            /*
             onCompletion() и onTimeout() регистрируют действия, которые требуется выполниь по завершении сеанса SSE,
             когда закончатся данные или обнаружится тайм-аут
             */
            onCompletion(() -> {
                log.info("[{}] SSE completed", sessionId);
                subscriber.unsubscribe();
            });
            onTimeout(() -> {
                log.info("[{}] SSE timeout", sessionId);
                subscriber.unsubscribe();
            });
        }

        Subscriber<Temperature> getSubscriber() {
            return subscriber;
        }

        int getSessionId() {
            return sessionId;
        }
    }

}
