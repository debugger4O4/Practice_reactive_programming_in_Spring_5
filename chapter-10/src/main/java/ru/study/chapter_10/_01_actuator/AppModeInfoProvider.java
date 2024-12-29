/**
 * Конечная точка для получения информации о службе.
 */

@Component
public class AppModeInfoProvider implements InfoContributor {
    private final Random rnd = new Random();

    @Override
    public void contribute(Info.Builder builder) {
        boolean appMode = rnd.nextBoolean();
        builder
                .withDetail("application-mode", appMode ? "experimental" : "stable");
    }
}