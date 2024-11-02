package ru.study.chapter_04._05_reactive_streams_life_cycle._01_assembly_stage;

/**
 * Этап сборки.
 */
public class AssemblyStage {
    // Сборка потока обработки без Reactor API.
    Flux<Integer> sourceFlux = new FluxArray(1, 20, 300, 4000);
    Flux<String> mapFlux = new FluxMap(sourceFlux, String::valueOf);
    Flux<String> sourceFlux = new FluxFilter(mapFlux, s -> s.length() > 1);
    ...

    // Цепочка издателей Publisher. just -> map -> filter.
    FluxFilter(
            FluxMap(
                    FluxArray(1, 2, 3, 40, 500, 6000);
            )
    )

    /*
     Превращение в один вызов поледовательность операторов concatWith -> concatWith -> concatWith.
     Если текущий экземпляр Flux относится к типу FluxConcatArray, то вместо
     FluxConcatArray(FluxConcatArray(FluxA, FluxB), FluxC) он создает один FluxConcatArray(FluxA, FluxB, FluxC) и тем
     самым улучшает общую производительность потока данных.
     */
    public final Flux<T> concatWith(Publisher<? extends T> other) {
        if (this instanceof FluxConcatArray) {
            @SuppressWarnings({"unchecked"})
            FluxConcatArray<T> fluxConcatArray = (FluxConcatArray<T>) this;

            return fluxConcatArray.concatAdditionalSourceLast(other);
        }
        return concat(this, other);
    }
}
