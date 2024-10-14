package ru.study.chapter_03._03_news_service;

import ru.study.chapter_03._03_news_service.dto.News;

import java.util.Date;
import java.util.Random;

public interface NewsHarness {
    Random RANDOM = new Random();

    static News generate() {
        return News.builder()
                .author(String.valueOf(RANDOM.nextGaussian()))
                .category("tech")
                .publishedOn(new Date())
                .content(String.valueOf(RANDOM.nextGaussian()))
                .title(String.valueOf(RANDOM.nextGaussian()))
                .build();
    }
}