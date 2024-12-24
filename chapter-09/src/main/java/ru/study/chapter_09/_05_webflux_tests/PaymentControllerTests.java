/**
 * Пример запуска нормального полного интеграционного тестирования.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class PaymentControllerTests {

    @Autowired
    WebTestClient webTestClient;
    ...
}