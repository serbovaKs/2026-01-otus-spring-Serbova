package ru.otus.hw.repository;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class AuthorRepositoryTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AuthorRepository authorRepository;

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
        var result = authorRepository.findById(id);
        assertTrue(result.isPresent());
        assertEquals(result.get().getFullName(), mongoTemplate.findById(id, Author.class).getFullName());
    }

    @Test
    public void testFindAll() {
        var result = authorRepository.findAll();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(
                mongoTemplate.findAll(Author.class).stream().map(Author::getFullName).toList(),
                result.stream().map(Author::getFullName).toList()
        );
    }

    @Test
    public void testSave() {
        var authorBySave = new Author("author_21");
        var resultSave = authorRepository.save(authorBySave);
        assertDoesNotThrow(() -> resultSave);
        assertEquals("author_21", resultSave.getFullName());
    }

    @Test
    public void testDelete() {
        assertDoesNotThrow(() -> authorRepository.delete(new Author(id, "author_1")));
    }
}
