/**
 * Обнаружение RabbitMQ в PCF.
 */

@Configuration
@Profile("cloud") // Включает конфигурацию RabbitMQ только при работе в облаке, а не локально во время разработки.
public class CloudConfig extends AbstractCloudConfig {

    @Bean
    public ConnectionFactory rabbitMQConnectionFactory() {
        return connectionFactory().rabbitConnectionFactory();
    }
}