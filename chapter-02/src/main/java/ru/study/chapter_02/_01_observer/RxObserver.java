package ru.study.chapter_02._01_observer;

/**
 * Реактивный наблюдатель
 * @param <T>
 */
public interface RxObserver<T> {
    // Клиент не вызывает метод onNext, как в Iterator. RxObserver сам уведомит клиента о появлении следующего значения
    void onNext(T next);
    /*
     Клиент не должен проверять результат вызова hasNext(). RxObserver сам сообщит об окончании потока данных обратным
     вызовом onComplete()
     */
    void onComplete();
    // Передача ошибки от производителя потребителю
    void onError(Exception e);
}
