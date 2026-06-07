package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.repositories.JpaGenreRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaAuthorRepository;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Сервис для работы с книгами")
@DataJpaTest
@Import({BookServiceImpl.class, JpaBookRepository.class, JpaAuthorRepository.class, JpaGenreRepository.class,
AuthorServiceImpl.class, GenreServiceImpl.class, BookConverter.class, AuthorConverter.class, GenreConverter.class})
public class BookServiceTest {
    @Autowired
    private BookService bookService;

    @Autowired
    private BookConverter bookConverter;

    @DisplayName("должен загружать полный список книг без ошибок")
    @Test
    void shouldAllBooks_Test() {
        assertDoesNotThrow(() -> bookService.findAll()
                .stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining())
        );
    }

    @DisplayName("должен загружать книгу по коду без ошибок")
    @Test
    void shouldBookById_Test() {
        assertDoesNotThrow(() -> bookService.findById(1)
                .stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining())
        );
    }
}
