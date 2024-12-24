/**
 * Проверка бзнес-логики реализации PaymentService.
 */

@Service
public class DefaultPaymentService implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final WebClient webClient;

    public DefaultPaymentService(PaymentRepository paymentRepository, WebClient.Builder client) {
        this.paymentRepository = repository;
        this.client = build.baseUrl("http://api.bank.com/submit").build();
    }

    @Override
    public Mono<String> send(Mono<Payment> payment) {
        return payment
                .zipWith(
                        ReactiveSecurityContextHolder.getContext(),
                        (p, c) -> p.withUser(c.getAuthentication().getName())
                )
                .flatMap(p -> client
                        .post()
                        .syncBody(p)
                        .retrieve()
                        .bodyToMono(String.class)
                        .then(paymentRepository.save(p)))
                .map(Payment::getId);
    }

    @Override
    public Flux<Payment> list() {
        return ReactiveSecurity
    }
}