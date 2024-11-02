package ru.study.chapter_06._04_samples._03_sse;

//import java.util.Map;
//
//import reactor.core.publisher.Flux;
//
//import org.springframework.http.codec.ServerSentEvent;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
/**
 * Реактивный поток SSE(Server-Sent Event) и легковесная замена WebSocket.
 */
@RestController
public class ServerSentController {

    private Map<String, StocksService> stringStocksServiceMap;

    @GetMapping("/sse/stocks")
    public Flux<ServerSentEvent<?>> streamStocks() {
        return Flux
                .fromIterable(stringStocksServiceMap.values())
                // Объединение всех доступных источников информации и передача их клиенту.
                .flatMap(StocksService::stream)
                // Преобразование каждого StockItem в ServerSentEvent.
                .<ServerSentEvent<?>>map(item ->
                        // Передача билдеру id и название события для настройки экземпляра.
                        ServerSentEvent
                                .builder(item)
                                .event("StockItem")
                                .id(item.getId())
                                .build()
                )
                /*
                 Запуск потока данных сконкретным экземпляром ServerSentEvent, который объявляет клиенту доступные
                 каналы информации.
                 */
                .startWith(
                        ServerSentEvent
                                .builder()
                                .event("Stocks")
                                .data(stringStocksServiceMap.keySet())
                                .build()
                );
    }
}
