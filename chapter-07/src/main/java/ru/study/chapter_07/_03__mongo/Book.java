package ru.study.chapter_07._03__mongo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Arrays;
import java.util.List;

@Document(collection = "book")
@Data
@NoArgsConstructor
public class Book {
    @Id
    private ObjectId id;

    @Indexed
    private String title;

    @Indexed
    private List<String> authors;

    @Field("pubYear")
    private int publishingYear;

    public Book(String title, int publishingYear, String... authors) {
        this.title = title;
        this.publishingYear = publishingYear;
        this.authors = Arrays.asList(authors);
    }
}