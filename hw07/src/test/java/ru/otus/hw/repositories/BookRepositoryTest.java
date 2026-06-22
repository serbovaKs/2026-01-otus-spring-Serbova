package ru.otus.hw.repositories;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository jpaBookRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        var actualBook = jpaBookRepository.findById(1L);
        var expectedBook = em.find(Book.class, 1);

        assertThat(actualBook).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        System.out.println("\n\n\n\n----------------------------------------------------------------------------------------------------------");

        assertThat(jpaBookRepository.findAll()).isNotNull().hasSize(3)
                .allMatch(b -> !b.getTitle().isEmpty())
                .allMatch(b -> b.getAuthor() != null)
                .allMatch(b -> b.getGenre() != null);
        System.out.println("----------------------------------------------------------------------------------------------------------\n\n\n\n");
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = new Book(0, "BookTitle_10500",
                new Author(1, "Author_1"), new Genre(1, "Genre_1"));
        var returnedBook = jpaBookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(returnedBook).isEqualTo(em.find(Book.class, returnedBook.getId()));
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = new Book(1L, "BookTitle_10500",
                new Author(2, "Author_2"), new Genre(2, "Genre_2"));

        var returnedBook = em.find(Book.class, expectedBook.getId());

        assertThat(returnedBook).isNotEqualTo(expectedBook);

        var savedBook = jpaBookRepository.save(expectedBook);
        assertThat(savedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(savedBook).isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        assertThat(em.find(Book.class, 1L)).isNotNull();
        jpaBookRepository.deleteById(1L);
        assertThat(em.find(Book.class, 1L)).isNull();
    }
}