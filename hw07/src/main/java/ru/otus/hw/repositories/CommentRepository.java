package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    @EntityGraph(attributePaths = "book")
    List<Comment> findByBookId(long bookId);
}
