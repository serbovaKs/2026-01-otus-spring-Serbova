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
import ru.otus.hw.models.Genre;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ExtendWith(SpringExtension.class)
public class GenreRepositoryTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    @AfterTestMethod
    void afterLoadDb() {
        DBObject objectToSave = BasicDBObjectBuilder.start()
                .add("_id", 1)
                .add("name", "Genre_1")
                .get();

        mongoTemplate.save(objectToSave, "Genre");
    }

    @Test
    public void testFindById() {
        var result = mongoTemplate.findById(1, Genre.class);
        assertNotNull(result);
        assertEquals("Genre_1", result.getName());
    }

    @Test
    public void testFindAll() {
        var result = mongoTemplate.findAll(Genre.class);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testSave() {
        var resultSave = mongoTemplate.save(new Genre(2, "Genre_21"));
        assertDoesNotThrow(() -> resultSave);
        assertEquals(mongoTemplate.findById(2, Genre.class).getName(), resultSave.getName());
    }

    @Test
    public void testDelete() {
        assertDoesNotThrow(() -> mongoTemplate.remove(new Genre(1, "Genre_1")).getDeletedCount());
        assertNull(mongoTemplate.findById(1, Genre.class));
    }
}
