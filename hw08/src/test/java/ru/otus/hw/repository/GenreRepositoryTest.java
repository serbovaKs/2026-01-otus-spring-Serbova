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
import ru.otus.hw.models.Genre;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ExtendWith(SpringExtension.class)
public class GenreRepositoryTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    private static String id;

    @BeforeAll
    public static void beforeLoadDb(@Autowired MongoTemplate mongoTemplate) {
        DBObject objectToSave = BasicDBObjectBuilder.start()
                .add("_id", ObjectId.get())
                .add("name", "genre_1")
                .get();

        id = mongoTemplate.save(objectToSave, "genre").get("_id").toString();
    }

    @Test
    public void testFindById() {
        var result = mongoTemplate.findById(id, Genre.class);
        assertNotNull(result);
        assertEquals("genre_1", result.getName());
    }

    @Test
    public void testFindAll() {
        var result = mongoTemplate.findAll(Genre.class);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testSave() {
        var resultSave = mongoTemplate.save(new Genre( "genre_21"));
        assertDoesNotThrow(() -> resultSave);
        assertEquals("genre_21", resultSave.getName());
    }

    @Test
    public void testDelete() {
        var result = mongoTemplate.remove(new Genre(id, "genre_1"), "genre");
        assertDoesNotThrow(() -> result);
        assertEquals(1, result.getDeletedCount());
    }
}
