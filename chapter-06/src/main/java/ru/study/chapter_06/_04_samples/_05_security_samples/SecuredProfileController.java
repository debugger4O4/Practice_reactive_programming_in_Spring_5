package ru.study.chapter_06._04_samples._05_security_samples;

//import reactor.core.publisher.Mono;
//
//import org.springframework.security.core.context.ReactiveSecurityContextHolder;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
/**
 * Реактивный доступ к SecurityContext.
 */
//@RestController
//@RequestMapping("/api/v1")
//public class SecuredProfileController {
//
//    private final ProfileService profileService;
//
//    public SecuredProfileController(ProfileService service) {
//        profileService = service;
//    }
//
//    @GetMapping("/profiles")
//    public Mono<Profile> getProfile() {
//        return ReactiveSecurityContextHolder
                // Получение доступа к текущему SecurityContext.
//                .getContext()
//                .map(SecurityContext::getAuthentication)
                // Получение доступа к профилю пользователя.
//                .flatMap(auth -> profileService.getByUser(auth.getName()));
//    }
//}
