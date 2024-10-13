package ru.study.chapter_03._03_news_service;

import ru.study.chapter_03._03_news_service.dto.NewsLetter;

import java.util.Collections;

public class StubNewsLetter extends NewsLetter {

    StubNewsLetter(int element) {
        super(String.valueOf(element), null, Collections.emptyList());
    }
}
