package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final JdbcOperations jdbc;
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public Optional<Book> findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        try {
            return Optional.ofNullable(namedParameterJdbcOperations.queryForObject(
                "select books.id, title, author_id, authors.full_name as author_full_name, genre_id, genres.name as genre_name " +
                        "from books " +
                        "join authors on authors.id = books.author_id " +
                        "join genres on genres.id = books.genre_id " +
                        "where books.id = :id",
                params,
                new BookRowMapper()
            ));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        return jdbc.query(
            "select books.id, title, author_id, authors.full_name as author_full_name, genre_id, genres.name as genre_name " +
                    "from books " +
                    "join authors on authors.id = author_id " +
                    "join genres on genres.id = genre_id",
            new BookRowMapper()
        );
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        namedParameterJdbcOperations.update(
                "delete books where id = :id", params
        );
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        params.addValue("genre_id", book.getGenre().getId());

        namedParameterJdbcOperations.update(
                "insert into books (title, author_id, genre_id) values (:title, :author_id, :genre_id)",
                params, keyHolder
        );
        //noinspection DataFlowIssue
        book.setId(keyHolder.getKey().longValue());
        return book;
    }

    private Book update(Book book) {
        int countUpdRow = namedParameterJdbcOperations.update(
                "update books set title = :title, author_id = :author_id, genre_id = :genre_id where id = :id",
                Map.of(
                        "id", book.getId(),
                        "title", book.getTitle(),
                        "author_id", book.getAuthor().getId(),
                        "genre_id", book.getGenre().getId()
                )
        );
        if (countUpdRow == 0) {
            throw new EntityNotFoundException("Error update book by id = %s".formatted(book.getId()));
        }

        // Выбросить EntityNotFoundException если не обновлено ни одной записи в БД
        return book;
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
