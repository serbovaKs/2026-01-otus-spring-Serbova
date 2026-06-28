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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataMongoTest
@ExtendWith(SpringExtension.class)
class AuthorRepositoryTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    private static String id;

    @BeforeAll
    public static void beforeLoadDb(@Autowired MongoTemplate mongoTemplate) {
        DBObject objectToSave = BasicDBObjectBuilder.start()
                .add("_id", ObjectId.get())
                .add("fullName", "author_1")
                .get();

        id = mongoTemplate.save(objectToSave, "author").get("_id").toString();
    }

    @Test
    public void testFindById() {
        var result = mongoTemplate.findById(id, Author.class);
        assertDoesNotThrow(() -> result);
        assertEquals("author_1", result.getFullName());
    }

    @Test
    public void testFindAll() {
        var result = mongoTemplate.findAll(Author.class);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testSave() {
        var resultSave = mongoTemplate.save(new Author("author_21"));
        assertDoesNotThrow(() -> resultSave);
        assertEquals("author_21", mongoTemplate.findById(resultSave.getId(), Author.class).getFullName());
    }

    @Test
    public void testDelete() {
        var result = mongoTemplate.remove(new Author(id, "author_1"), "author");
        assertDoesNotThrow(() -> result);
        assertEquals(1, result.getDeletedCount());
    }
}
