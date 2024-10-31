package ru.study.chapter_06._10_memory_consumption_of_different_processing_models;

//@RestController
//@RequestMapping("api/json")
//public class BigJSONProcessorController {
    /*
    Использование такой модели обработки позволяет начать возврат ответа намного быстрее, поскольку время между отправкой
    клиентом первого элемента и получением ответа R = Rnet + Rprocessing + Rnet.
     */
//    @GetMapping(value = "/process-json", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
//    public Flux<ProcessedItem> processOneByOne(Flux<Item> bodyFlux) {
//        return bodyFlux
//                .map(item -> processItem(item))
//                .filter(processedItem -> fiterItem(processedItem));
//    }
    // Среднее время обработки  = N * (Rnet + Rprocessing) + Rnet.
//    @GetMapping(value = "/process-json")
//    public List<ProcessedItem> processOneByOne(List<Item> bodyList) {
//        return bodyList
//                .stream()
//                .map(item -> processItem(item))
//                .filter(processedItem -> fiterItem(processedItem))
//                .collect(toList());
//    }
//}
