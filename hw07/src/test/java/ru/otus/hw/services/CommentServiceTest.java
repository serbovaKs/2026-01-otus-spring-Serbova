package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.converters.GenreConverter;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@DisplayName("Сервис для работы с комментариями")
@DataJpaTest
@Transactional(propagation = Propagation.NEVER)
@Import({CommentServiceImpl.class, CommentConverter.class, BookConverter.class, AuthorConverter.class, GenreConverter.class})
public class CommentServiceTest {

    @Autowired
    private CommentService service;

    @Autowired
    private CommentConverter converter;

    @DisplayName("должен загружать полный список комментариев к книге без ошибок")
    @Test
    void shouldAllCommentByBookId_Test() {
        assertDoesNotThrow(() -> service.findByBookId(1)
                .stream()
                .map(converter::commentToString)
                .collect(Collectors.joining())
        );
    }

    @DisplayName("должен загружать комментарий по коду без ошибок")
    @Test
    void shouldCommentById_Test() {
        assertDoesNotThrow(() -> service.findById(1)
                .stream()
                .map(converter::commentToString)
                .collect(Collectors.joining())
        );
    }
}
