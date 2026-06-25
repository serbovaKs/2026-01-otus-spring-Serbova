package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, Long> {

    @Query("{ 'book_id' : :#{#bookId} }")
    List<Comment> findByBookId(long bookId);
}
