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
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import static org.junit.jupiter.api.Assertions.*;


@DataMongoTest
@ExtendWith(SpringExtension.class)
public class BookRepositoryTest {
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
        mongoTemplate.save(objectToSave, "Genre");

        objectToSave = BasicDBObjectBuilder.start()
                .add("_id", 1)
                .add("title", "Book_1")
                .add("author_id", 1)
                .add("genre_id", 1)
                .get();
        mongoTemplate.save(objectToSave, "book");
    }

    @Test
    public void testFindById() {
        var result = mongoTemplate.findById(1, Book.class);
        assertNotNull(result);
        assertEquals("Book_1", result.getTitle());
        assertEquals(1, result.getAuthor().getId());
        assertEquals(1, result.getGenre().getId());
    }

    @Test
    public void testFindAll() {
        var result = mongoTemplate.findAll(Book.class);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testSave() {
        var resultSave = mongoTemplate.save(
                new Book(2, "Book_21", new Author(1, "Author_1"), new Genre(1, "Genre_1"))
        );
        assertDoesNotThrow(() -> resultSave);
        assertEquals(mongoTemplate.findById(2, Book.class).getTitle(), resultSave.getTitle());
    }

    @Test
    public void testDelete() {
        assertDoesNotThrow(() -> mongoTemplate.remove(
                new Book(1, "Book_1", new Author(1, "Author_1"), new Genre(1, "Genre_1"))
        ).getDeletedCount());
        assertNull(mongoTemplate.findById(1, Book.class));
    }
}
