package ru.otus.hw.repository;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
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

    private static String authorId;
    private static String genreId;
    private static String bookId;

    @BeforeAll
    public static void beforeLoadDb(@Autowired MongoTemplate mongoTemplate) {
        DBObject objectToSaveAuthor = BasicDBObjectBuilder.start()
                .add("_id", ObjectId.get())
                .add("fullName", "author_1")
                .get();
        authorId = mongoTemplate.save(objectToSaveAuthor, "author").get("_id").toString();

        DBObject objectToSaveGenre = BasicDBObjectBuilder.start()
                .add("_id", ObjectId.get())
                .add("name", "genre_1")
                .get();
        genreId = mongoTemplate.save(objectToSaveGenre, "genre").get("_id").toString();

        DBObject objectToSave = BasicDBObjectBuilder.start()
                .add("_id", ObjectId.get())
                .add("title", "book_1")
                .add("author_id", authorId)
                .add("genre_id", genreId)
                .get();
        bookId = mongoTemplate.save(objectToSave, "book").get("_id").toString();
    }

    @Test
    public void testFindById() {
        var result = mongoTemplate.findById(bookId, Book.class);
        assertNotNull(result);
        assertEquals("book_1", result.getTitle());
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
                new Book("book_21", new Author(authorId,"author_1"), new Genre(genreId, "genre_1"))
        );
        assertDoesNotThrow(() -> resultSave);
        assertEquals("book_21", mongoTemplate.findById(resultSave.getId(), Book.class).getTitle());
    }

    @Test
    public void testDelete() {
        var result = mongoTemplate.remove(
                new Book(bookId, "book_1", new Author(authorId, "author_1"), new Genre(genreId, "genre_1"))
        );
        assertDoesNotThrow(() -> result);
        assertEquals(1L, result.getDeletedCount());
    }
}
