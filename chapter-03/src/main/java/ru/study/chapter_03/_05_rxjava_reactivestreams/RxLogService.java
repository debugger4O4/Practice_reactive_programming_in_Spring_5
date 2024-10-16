package ru.study.chapter_03._05_rxjava_reactivestreams;

/*
Косяк с импортами
 */
//import java.net.InetSocketAddress;
//
//import io.netty.buffer.ByteBuf;
//import io.reactivex.netty.protocol.http.client.HttpClient;
//import io.reactivex.netty.protocol.http.client.HttpClientResponse;
//import io.reactivex.netty.protocol.http.sse.ServerSentEvent;
//import io.reactivex.rxjava3.core.Observable;
//import org.reactivestreams.Publisher;
//
//
//import org.springframework.stereotype.Service;

/**
 * Старая реализация на основе Rx
 */
//@Service
//public class RxLogService implements LogService {
      /*
      HttpClient из RxNetty поддерживает асинхронные взаимодействия с внешними службами неблокирующим способом,
      применяя Netty Client, завернутый в API на основе RxJava
       */
//    final HttpClient<ByteBuf, ByteBuf> rxClient =
//            HttpClient.newClient(new InetSocketAddress(8080));
//
//    @Override
//    public Publisher<String> stream() {
          /*
          Отправка внешнего запроса. С помощью созданного HttpClient внешней службе посылается запрос на получение
          потока записей из журанала, элементы которого преобразуются в экземпляр String
           */
//        Observable<String> rxStream = rxClient.createGet("/logs")
//                .flatMap(HttpClientResponse::getContentAsServerSentEvents)
//                .map(ServerSentEvent::contentAsString);
          /*
          С помощью RxReactiveStreams rxStream преобразуется в Publisher
           */
//        return RxReactiveStreams.toPublisher(rxStream);
//    }
//}
