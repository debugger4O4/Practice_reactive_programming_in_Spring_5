package ru.study.chapter_05._04_reactive_input_output;

public class ReactiveFileReader {

//    public Flux<DataBuffer> backpressuredShakespeare() {
//        return DataBufferUtils
//                .read(
//                        new DefaultResourceLoader().getResource("hamlet.txt"),
//                        new DefaultDataBufferFactory(),
//                        1024
//                )
//                .log();
//    }
}

// Реактивные кодеки.
//interface Encoder<T> {
//    ...
//    Flux<DataBuffer> encode(Publisher<? extends T> inputStream, ...);
//}

//interface Decoder<T> {
//    ...
//    Flux<T> decode(Publisher<DataBuffer> inputStream, ...);
//    Mono<T> decodeToMono(Publisher<DataBuffer> inputStream, ...);
//}