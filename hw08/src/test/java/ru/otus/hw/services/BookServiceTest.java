package ru.otus.hw.services;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.event.annotation.AfterTestMethod;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataMongoTest
@DisplayName("Сервис для работы с книгами")
@Transactional(propagation = Propagation.NEVER)
@Import({BookServiceImpl.class, BookConverter.class, AuthorConverter.class, GenreConverter.class})
public class BookServiceTest {
    @Autowired
    private BookService bookService;

    @Autowired
    private BookConverter bookConverter;

    @AfterTestMethod
    void afterLoadDb(@Autowired MongoTemplate mongoTemplate) {
        DBObject objectToSave = BasicDBObjectBuilder.start()
            .add("_id", 1)
                .add("fullName", "Author_1")
                .get();
        mongoTemplate.save(objectToSave, "author");

        objectToSave = BasicDBObjectBuilder.start()
                .add("_id", 1)
                .add("name", "Genre_1")
                .get();
        mongoTemplate.save(objectToSave, "genre");

        objectToSave = BasicDBObjectBuilder.start()
                .add("_id", 1)
                .add("title", "Book_1")
                .add("author_id", 1)
                .add("genre_id", 1)
                .get();
        mongoTemplate.save(objectToSave, "Book");
    }

    @DisplayName("должен загружать полный список книг без ошибок")
    @Test
    void shouldAllBooks_Test() {
        assertDoesNotThrow(() -> bookService.findAll()
                .stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining())
        );
    }

    @DisplayName("должен загружать книгу по коду без ошибок")
    @Test
    void shouldBookById_Test() {
        assertDoesNotThrow(() -> bookService.findById(1)
                .stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining())
        );
    }
}
