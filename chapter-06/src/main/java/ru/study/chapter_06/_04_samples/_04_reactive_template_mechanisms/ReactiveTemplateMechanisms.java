package ru.study.chapter_06._04_samples._04_reactive_template_mechanisms;

/**
 * Реактивные механизмы шаблонов.
 */
public class ReactiveTemplateMechanisms {

    // Способ визуализации.
//    @RequestMapping("/")
//    public String index() {
//        return "index";
//    }

    // Реактивный способ визуализации FreeMarker.
//    @RequestMapping("/play-list-view")
//    public Mono<String> getPlaylist(final Model model) {
        // Замена dataSource списком Song.
//        final Flux<Song> playlistStream = ...;
//        return playlistStream
//                .collectionList()
                // Добавление требуемого атрибута в модель.
//                .doOnNext(list -> model.addAttribute("playList", list))
                // Сбор и запись всех атрибутов в модель.
//                .then(Mono.just("freemarker/play-list-view"));
//    }

    // Реактивный способ визуализации Thymeleaf.
//    @RequestMapping("/play-list-view")
//    public String view(final Model model) {
//        final Flux<Song> playlistStream = ...;
//        model.addAttribute(
//                "playList",
                /*
                 ReactiveDataDriverContextVariable принимает реактивные типы, такие как Publisher, Flux, Mono, Observable
                 и другие, поддерживаемые классом ReactiveAdapterRegister.
                 */
//                new ReactiveDataDriverContextVariable(playlistStream, 1, 1)
//        );
//        return "thymeleaf/play-list-view";
//    }
    /*
    <!DOCTYPE html>
    <html>
        ...
        <body>
            ...
            <table>
                <thread>
                ...
                <thread>
                <tbody>
                    <tr th:each="e : ${playList}">
                        <td th:text="${e.id}">...</td>
                        <td th:text="${e.name}">...</td>
                        <td th:text="${e.artlist}">...</td>
                        <td th:text="${e.album}">...</td>
                    </tr>
                </tbody>
            </table>
        </body>
    </html>
     */
}
