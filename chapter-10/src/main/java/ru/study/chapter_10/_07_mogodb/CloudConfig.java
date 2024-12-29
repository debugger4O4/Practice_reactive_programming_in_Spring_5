/**
 * Обнаружение MongoDB в PCF.
 */

@Configuratiuon
@Profile("cloud")
public class CloudConfig extends AbstractCloudConfig {
    ...

    // Типичный подход к проверке присутствия MongoClient в пути поиска классов(classpath).
    @Configuration
    @ConditionalOnClass(MongoClient.class)
    @EnableConfigurationProperties(MongoProperties.class)
    public class MongoCloudConfig extends MongoReactiveAutoConfiguration {
        ...

        @Bean
        @Override
        public MongoClient reactiveStreamsMongoClient(
                MongoProperties properties,
                Environment environment,
                ObjectProvider<List<MongoClientSettingsBuilderCustomizer>> builderCustomizers
        ) {
            // Конфигурация реактивного компонента MongoClient.
            List<ServiceInfo> infos = cloud()
                    .getServiceInfos(MongoDbFactory.class); // Получение информации о MongoDB из облачного контейнера.

            if (infos.size() == 1) {
                MongoServiceInfo mongoInfo =
                        (MongoServiceInfo) infos.get(0);
                properties.setUri(mongoInfo.getUri());
            }

            // Создание нового реактивного потока клинета MongoDB.
            return super.reactiveStreamsMongoClient(
                    properties,
                    environment,
                    builderCustomizers
            );
        }
    }
}