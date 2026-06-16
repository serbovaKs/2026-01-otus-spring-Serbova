package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaCommentRepository;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@DisplayName("Сервис для работы с комментариями")
@DataJpaTest
@Import({CommentServiceImpl.class, JpaBookRepository.class, JpaCommentRepository.class, CommentConverter.class})
@Transactional(propagation = Propagation.NEVER)
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
