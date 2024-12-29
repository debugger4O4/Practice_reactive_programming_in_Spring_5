/**
 * Собственная реализация Schedulers.Factory.
 */

public class MeteredSchedulersFactory implements Schedulers.Factory {
    private final MeterRegistry registry;

    // Декодирование запланированной службы исполнителя.
    public ScheduledExecutorService decorateExecutorService(
            String type, // Тип планировщика.
            Supplier<? extends ScheduledExecutorService> actual /*
                                                                   Ссылка на фактическую службу, которая должна быть
                                                                   декодирована.
                                                                */
    ) {
        ScheduledExecutorService actualScheduler = actual.get(); // Извлечение экземпляра службы исполнителя.
        String metric = "scheduler." + type + ".execution"; // Определение имени счетчика, включающего тип планировщика Scheduler.

        /*
         Объявление анонимного экземпляра ScheduledExecutorService, который декорирует фактический
         ScheduledExecutorService и предлагает дополнительные возможности.
         */
        ScheduledExecutorService scheduledExecutorService = new ScheduledExecutorService() {
            @Override
            public void execute(Runnable command) {
                registry.counter(metric, "type", "execute").increment();
                actualScheduler.execute(command);
            }

            @Override
            public <T> Future<T> submit(Callable<T> task) {
                registry.counter(metric, "type", "submit").increment();
                return actualScheduler.submit(task);
            }
        };

            // Другие переопределенные методы...

        // Регистрация счетчика созданных экземпляров службы исполнителя.
        registry.counter("scheduler." + type + ".instances").increment();
        return scheduledExecutorService;
    }
}