/**
 * Упрощенная интеграция с Spring Cloud Discovery для масштабируемости.
 */

public class SpringCloudDiscoveryIntegration {

    Flux
            .interval(Duration.ofMillis(100)) /*
                                                interval() - периодически извлекает доступные экземпляры с некоторым
                                                идентификатором serviceId.
                                              */
            .map(i ->
                discoveryClient
                        .getInstances(serviceId) // Извлечение экземпляров.
                        .stream()
                        .map(si ->
                            new RSocketSupplier(() ->
                                RSocketFactory.connect()
                                    .transport(
                                            TcpClientTransport.create(
                                                    si.getHost(),
                                                    si.getPort()
                                            )
                                    )
                                    .start()
                            ) {
                                public boolean equals(Object obj) { ... }

                                public int hashCode() {
                                    return si.getUri().hashCode();
                                }
                            }
                        )
                        .collect(toCollection(ArrayList<RSocketSupplier>::new))
            )
            .as(LoadBalancedRSocketMono::create);
}