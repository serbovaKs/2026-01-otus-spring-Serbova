package ru.otus.hw.repository;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class GenreRepositoryTest {
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Autowired
    private GenreRepository genreRepository;

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
        var result = genreRepository.findById(id);
        assertTrue(result.isPresent());
        assertEquals(result.get().getName(), mongoTemplate.findById(id, Genre.class).getName());
    }

    @Test
    public void testFindAll() {
        var result = genreRepository.findAll();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(
                mongoTemplate.findAll(Genre.class).stream().map(Genre::getName).toList(),
                result.stream().map(Genre::getName).toList()
        );
    }

    @Test
    public void testSave() {
        var GenreBySave = new Genre("genre_21");
        var resultSave = genreRepository.save(GenreBySave);
        assertDoesNotThrow(() -> resultSave);
        assertEquals("genre_21", resultSave.getName());
    }

    @Test
    public void testDelete() {
        assertDoesNotThrow(() -> genreRepository.delete(new Genre(id, "genre_1")));
    }
}
