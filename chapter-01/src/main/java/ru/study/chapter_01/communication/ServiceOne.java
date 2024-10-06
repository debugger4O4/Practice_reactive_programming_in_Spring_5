package ru.study.chapter_01.communication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.study.chapter_01.commons.ExamplesCollection;

/**
 * Пример блокировки связи
 */
@RestController
// Объявление обработчика запросов
@RequestMapping("api/v1/resource/a")
public class ServiceOne {
    private static final String PORT = "8080";

    @GetMapping
    public ExamplesCollection processRequest() {
        // Создание веб-клиента для организации взаимодействия типа "запрос-ответ"
        RestTemplate template = new RestTemplate();
        // Конструирование и посылка GET-запроса
        ExamplesCollection result = template.getForObject(
                "http://localhost:" + PORT + "/api/v1/resource/b",
                ExamplesCollection.class
        );

        // Дополнительные операции

        // Следующий этап обработки ответа
        processResultFurther(result);

        return result;
    }

    // Дополнительные операции
    private void processResultFurther(ExamplesCollection result) {
        // Do some processing
    }
}
