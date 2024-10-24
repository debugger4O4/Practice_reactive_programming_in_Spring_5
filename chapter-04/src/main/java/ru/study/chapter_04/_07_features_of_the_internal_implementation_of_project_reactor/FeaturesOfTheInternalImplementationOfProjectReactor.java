package ru.study.chapter_04._07_features_of_the_internal_implementation_of_project_reactor;

/**
 * Особенности внутренней реализации Project Reactor.
 */
public class FeaturesOfTheInternalImplementationOfProjectReactor {

    /**
     * Макрослияние - оптимизация на этапе сборки.
     */
    // Обработка элемента передается для обработки в другой поток выполнения сразу после его создания.
//    Flux.just(1)              |
//            .publishOn(...)   |> дорого.
//            .map(...)         |

//    Flux.just(1)              |
//            .subscribeOn(...)   |> дешего.
//            .map(...)         |
    /**
     * Микрослияние - оптимизация этапа выполнения и повторное использование ресурсов.
     */
    /*
     filter() - серьезное влияние на производительность. Посылает издателю дополнителный запрос request(1). Каждый вызов
     request() обычно влечет выполнение атомарного цикла сравнения с обменом (Compare and Swap, CAS) длительностью 21-45
     тактов на каждый элемент (Исследования Дэвида Карнока).
     ConditionalSubscriber - организовывает проверку условия на стороне источника и передачу необходимого количества
     элементов без дополнительных вызовов request().
     */
//    Flux.from(factory)
//            .filter(inspectionDepartment)
//            .subscribe(store);

    // Цепочка операторов с несколькими асинхронными границами.
//    Flux.just(1, 2, 3)
//            .publishOn(Schedulers, parallel())
//            .concatMap(i -> Flux.range(0, i)
//                .publishOn(Schedulers.parallel()))
//            .subscribe();
}
