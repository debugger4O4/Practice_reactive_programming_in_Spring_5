/**
 * Модульная организация Spring Cloud Function.
 */

@SpringBootApplication
@EnableBinding(Processor.class)
public class Application {

    // Преобразование одного потока данных в другой.
    @Bean
    public Function<
            Flux<Payment>,
            Flux<Payment>
            > validate() {
        return flux -> flux.map(value -> { ... })

    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}