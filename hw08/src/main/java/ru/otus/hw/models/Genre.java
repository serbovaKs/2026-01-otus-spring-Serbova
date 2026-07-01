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
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Genre {
    @MongoId(FieldType.OBJECT_ID)
    private String id;

    private String name;

    public Genre(String name) {
        this.name = name;
    }
}
