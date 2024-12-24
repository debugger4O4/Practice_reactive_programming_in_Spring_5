/**
 * Тестирование WebFlux.
 * Тестирование контроллеров с помощью WebTestClient.
 */

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/")
    public Flux<Payment> list() {
        return paymentService.list();
    }

    @PostMapping("/")
    public Mono<String> send(Mono<Payment> payment) {
        return paymentService.send(payment);
    }
}