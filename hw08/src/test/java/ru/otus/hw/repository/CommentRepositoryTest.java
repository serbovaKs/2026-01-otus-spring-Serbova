package ru.otus.hw.repository;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.event.annotation.AfterTestMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DataMongoTest
@ExtendWith(SpringExtension.class)
public class CommentRepositoryTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    @AfterTestMethod
    void afterLoadDb() {
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

        objectToSave = BasicDBObjectBuilder.start()
                .add("_id", 1)
                .add("text", "Comment_1")
                .add("book_id", 1)
                .get();
        mongoTemplate.save(objectToSave, "comment");
    }

    @Test
    public void testFindById() {
        var result = mongoTemplate.findById(1, Comment.class);
        assertNotNull(result);
        assertEquals("Comment_1", result.getText());
        assertEquals(1, result.getBook().getId());
    }

    @Test
    public void testFindAll() {
        var result = mongoTemplate.findAll(Comment.class);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testSave() {
        var resultSave = mongoTemplate.save(
                new Comment(2, "Comment_21", mongoTemplate.findById(1, Book.class))
        );
        assertDoesNotThrow(() -> resultSave);
        assertEquals(mongoTemplate.findById(2, Comment.class).getText(), resultSave.getText());
    }

    @Test
    public void testDelete() {
        assertDoesNotThrow(() -> mongoTemplate.remove(
                new Comment(1, "Comment_1", mongoTemplate.findById(1, Book.class))
        ).getDeletedCount());
        assertNull(mongoTemplate.findById(1, Comment.class));
    }
}
