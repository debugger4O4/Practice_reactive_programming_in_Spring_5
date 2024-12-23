## Глава 7: Реактивный доступ к базе данных

* `_01_jdbc` — блокирующее консольное приложение, демонстрирующее библиотеку Jdbi, Spring JDBC и Spring Data JDBC (запускает встроенную базу данных H2).
* `_02_jpa` — блокирующее консольное приложение, демонстрирующее Spring Data JPA (запускает встроенную базу данных H2).
* `_03_mongo` — блокирующее консольное приложение, демонстрирующее Spring Data MongoDB (запускает встроенную MongoDB)
* `_04_rx_mongo` — реактивное консольное приложение, демонстрирующее реактивную Spring Data MongoDB (запускает встроенную MongoDB).
* `_05_rx_mongo_tx` — модульные тесты, демонстрирующие, как реактивные транзакции работают с MongoDB 4 (запускает кластер MongoDB в Docker).
* `_06_r2dbc` — реактивное консольное приложение, демонстрирующее совместную работу Spring Data JDBC и **R2DBC** (запускает PostgreSQL в докере).
* `_07_rx_webflux` — реактивное веб_приложение (SSE), имитирующее простое приложение чата (встроенная MongoDB).
* `_08_rx_dbs` — примеры кода, демонстрирующие различные типы реактивных репозиториев (MongoDB, Cassandra, Couchbase, Redis).
* `_09_rx_sync` — консольное приложение, которое демонстрирует, как обернуть блокирующий репозиторий JPA в реактивный репозиторий (запускает встроенную базу данных H2).
* `_10_rxjava2_jdbc` — консольное приложение, которое демонстрирует, как использовать библиотеку `rxjava2_jdbc` для реактивного взаимодействия поверх блокировки JDBC (запускает встроенную базу данных H2).