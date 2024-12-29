/**
 * Мониторинг планировщиков в Reactor.
 */

public class MeteredScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {

    public MeteredScheduledThreadPoolExecutor(
            int corePoolSize,
            MeterRegistry registry
    ) {
        super(corePoolSize);
        // Регистрация датчиков.
        registry.gauge(meterName("size"), this.getCorePoolSize());  // Датчик размера пула потоков.
        registry.gauge(meterName("active"), this.getActiveCount()); // Датчик активных заданий.
        registry.gauge(meterName("queue.size"), getQueue().size()); // Датчик размера очереди.
    }
}