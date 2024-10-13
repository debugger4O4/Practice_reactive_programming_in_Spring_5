package ru.study.chapter_03._03_news_service.dto;

import lombok.*;

import java.util.Collection;

@With
@Data(staticConstructor = "of")
@Builder(builderClassName = "NewsLetterTemplate", builderMethodName = "template")
@AllArgsConstructor
public class NewsLetter {

    private final @NonNull String title;
    private final          String recipient;
    private final @NonNull Collection<News> digest;
}