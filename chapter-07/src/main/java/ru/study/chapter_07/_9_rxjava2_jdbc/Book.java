package ru.study.chapter_07._9_rxjava2_jdbc;

/**
 * С помощью библиотеки rxjava2-jdbc.
 */
@Query("select id, title, publishing_year from book order by publishing_year")
public interface Book {
    @Column String id();
    @Column String title();
    @Column Integer publishing_year();
}
