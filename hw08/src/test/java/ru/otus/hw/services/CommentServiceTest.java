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
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.converters.GenreConverter;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataMongoTest
@DisplayName("Сервис для работы с комментариями")
@Transactional(propagation = Propagation.NEVER)
@Import({CommentServiceImpl.class, CommentConverter.class, BookConverter.class, AuthorConverter.class, GenreConverter.class})
public class CommentServiceTest {

    @Autowired
    private CommentService service;

    @Autowired
    private CommentConverter converter;

    @AfterTestMethod
    void afterLoadDb(@Autowired MongoTemplate mongoTemplate) {
        DBObject objectToSave = BasicDBObjectBuilder.start()
                .add("_id", 1)
                .add("title", "Book_1")
                .add("author_id", 1)
                .add("genre_id", 1)
                .get();
        mongoTemplate.save(objectToSave, "Book");

        objectToSave = BasicDBObjectBuilder.start()
                .add("_id", 1)
                .add("text", "Comment_1")
                .add("book_id", 1)
                .get();
        mongoTemplate.save(objectToSave, "comment");
    }

    @DisplayName("должен загружать полный список комментариев к книге без ошибок")
    @Test
    void shouldAllCommentByBookId_Test() {
        assertDoesNotThrow(() -> service.findByBookId(1)
                .stream()
                .map(converter::commentToString)
                .collect(Collectors.joining())
        );
    }

    @DisplayName("должен загружать комментарий по коду без ошибок")
    @Test
    void shouldCommentById_Test() {
        assertDoesNotThrow(() -> service.findById(1)
                .stream()
                .map(converter::commentToString)
                .collect(Collectors.joining())
        );
    }
}
