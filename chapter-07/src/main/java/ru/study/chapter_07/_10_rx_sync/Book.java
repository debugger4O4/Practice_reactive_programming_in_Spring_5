package ru.study.chapter_07._10_rx_sync;

/**
 * Обертывание синхронного CrudRepository.
 */
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private Integer publishingYear;

    // Конструкторы, методы свойств...
}
