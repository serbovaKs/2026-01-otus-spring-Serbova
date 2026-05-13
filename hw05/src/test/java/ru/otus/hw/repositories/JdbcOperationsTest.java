package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@JdbcTest
@Import({JdbcBookRepository.class, JdbcGenreRepository.class})
public class JdbcOperationsTest {
    @Autowired
    private JdbcOperations jdbc;
    @Autowired
    private NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Test
    void findById() {
        Book book = namedParameterJdbcOperations.queryForObject(
                "select books.id, title, author_id, authors.full_name as author_full_name, genre_id, genres.name as genre_name " +
                        "from books " +
                        "join authors on authors.id = books.author_id " +
                        "join genres on genres.id = books.genre_id " +
                        "where books.id = :id",
                Collections.singletonMap("id", 1),
                new BookRowMapper()
        );
        assertNotNull(book);
    }

    @Test
    void findAll() {
        List<Book> list = jdbc.query(
                "select books.id, title, author_id, authors.full_name as author_full_name, genre_id, genres.name as genre_name " +
                        "from books " +
                        "join authors on authors.id = author_id " +
                        "join genres on genres.id = genre_id",
                new BookRowMapper()
        );
        assertThat(list).isNotEmpty();
    }

    @Test
    void insert() {
        var keyHolder = new GeneratedKeyHolder();
        var params = new MapSqlParameterSource(Map.of(
                "title", "Book_1",
                "author_id", 1,
                "genre_id", 1
        ));

        assertThat(namedParameterJdbcOperations.update(
                "insert into books (title, author_id, genre_id) values (:title, :author_id, :genre_id)",
                params, keyHolder
        )).isEqualTo(1);
    }

    @Test
    void update_no_exception() {
        var result = namedParameterJdbcOperations.update(
                "update books set title = :title, author_id = :author_id, genre_id = :genre_id where id = :id",
                Map.of(
                        "id", 1,
                        "title", "Book_1",
                        "author_id", 1,
                        "genre_id", 1
                )
        );
        assertDoesNotThrow(() -> result);
        assertThat(result).isNotEqualTo(0);
    }

    @Test
    void update_exception() {
        var result = namedParameterJdbcOperations.update(
                "update books set title = :title, author_id = :author_id, genre_id = :genre_id where id = :id",
                Map.of(
                        "id", 10,
                        "title", "Book_1",
                        "author_id", 1,
                        "genre_id", 1
                )
        );
        assertDoesNotThrow(() -> result);
        assertThat(result).isEqualTo(0);
    }

    @Test
    void delete_by_id() {
        assertDoesNotThrow(() -> namedParameterJdbcOperations.update(
                "delete books where id = :id",
                Map.of("id", 1)
        ));
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String title = rs.getString("title");

            long authorId = rs.getLong("author_id");
            String authorFullName = rs.getString("author_full_name");
            Author author = new Author(authorId, authorFullName);

            long genreId = rs.getLong("genre_id");
            String genreName = rs.getString("genre_name");
            Genre genre = new Genre(genreId, genreName);

            return new Book(id, title, author, genre);
        }
    }
}
