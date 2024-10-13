package ru.study.chapter_03._03_news_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.annotations.Immutable;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
@Immutable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class News {

    @Id
    @JsonIgnore
    private ObjectId id;

    private @NonNull String title;
    private @NonNull String content;
    private @NonNull Date publishedOn;
    private @NonNull String category;
    private @NonNull String author;
}
