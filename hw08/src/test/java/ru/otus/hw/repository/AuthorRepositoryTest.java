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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;


@DataMongoTest
@ExtendWith(SpringExtension.class)
public class AuthorRepositoryTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    @AfterTestMethod
    void afterLoadDb() {
        DBObject objectToSave = BasicDBObjectBuilder.start()
                .add("_id", 1)
                .add("fullName", "Author_1")
                .get();

        mongoTemplate.save(objectToSave, "author");
    }

    @Test
    public void testFindById() {
        var result = mongoTemplate.findById(1, Author.class);
        assertNotNull(result);
        assertEquals("Author_1", result.getFullName());
    }

    @Test
    public void testFindAll() {
        var result = mongoTemplate.findAll(Author.class);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testSave() {
        var resultSave = mongoTemplate.save(new Author(2, "Author_21"));
        assertDoesNotThrow(() -> resultSave);
        assertEquals(mongoTemplate.findById(2, Author.class).getFullName(), resultSave.getFullName());
    }

    @Test
    public void testDelete() {
        assertDoesNotThrow(() -> mongoTemplate.remove(new Author(1, "Author_1")).getDeletedCount());
        assertNull(mongoTemplate.findById(1, Author.class));
    }
}
