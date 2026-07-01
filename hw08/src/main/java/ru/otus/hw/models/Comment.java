package ru.otus.hw.models;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Comment {
    @MongoId(FieldType.OBJECT_ID)
    private String id;

    private String text;

    private Book book;

    public Comment(String text, Book book) {
        this.book = book;
        this.text = text;
    }
}
