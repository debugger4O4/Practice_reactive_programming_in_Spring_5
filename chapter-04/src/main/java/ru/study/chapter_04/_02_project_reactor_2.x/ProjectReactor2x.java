package ru.study.chapter_04._02_project_reactor_2.x;

/**
 * Project Reactor 2.x.
 */
public class ProjectReactor2x {

    stream()
          /*
          retry() - увеличение устойчивости процесса обработки, т.е. когда в потоке возникает ошибка, операции в нем
          повторяются.
           */
        .retry()
          /*
          onOverflowBuffer() и onOverflowDrop() добавляют управление обратным давлением для случая, когда издатель
          поддерживает только модель PUSH(и не учитывается спрос потребителя).
           */
        .onOverflowBuffer()
        .onOverflowDrop()
          /*
          dispatchOn() - выделение нового диспетчера Dispatcher для работы с этим реактивным потоком данных. Т.о.
          обеспечивается возможность асинхронной обработки сообщений.
           */
        .dispatchOn(new RingBufferDispatcher("test"))

}
