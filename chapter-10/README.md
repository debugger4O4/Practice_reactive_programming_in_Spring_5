# Глава 10: И наконец, выпусти это!

В этом примере изображено реактивное приложение на базе Spring Boot 2 и WebFlux со всей необходимой инфраструктурой для оперативного мониторинга.

Метрики собирает Prometheus, у Grafana есть простая панель с метриками приложений, Zipkin собирает трассировки.

## Структура приложения

- Само приложение:
    - Реактивное веб-приложение (с администратором Spring Boot)
    - База данных: MongoDB (не используется)
- Инфраструктура мониторинга:
    - Prometheus
    - Grafana
    - Zipkin

## Запуск или остановка инфраструктурных служб

Для запуска служб выполните следующую команду:

``` баш
docker-compose -f docker/docker-compose.yml up -d
```

Чтобы остановить службы, выполните следующую команду:

``` баш
docker-compose -f docker/docker-compose.yml вниз
```

## Запуск приложения Spring Boot

Чтобы запустить приложение, запустите свой любимый класс IDE.
`chapter_10.Chapter10CApplication`.

## Доступ к компонентам приложения

- Реактивное веб-приложение: <localhost:8080>
- Spring Boot Admin 2.0: <localhost:8090/admin>
- Prometheus: <localhost:9090>
- Grafana: <localhost:3000> (пользователь: `admin`, пароль: `admin`)
- Zipkin: <localhost:9411>