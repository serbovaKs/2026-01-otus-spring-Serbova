package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "root", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAll", author = "root")
    public void insertAll(
            CommentRepository commentRepository, BookRepository bookRepository,
            AuthorRepository authorRepository, GenreRepository genreRepository) {
        var authors = authorRepository.saveAll(
            List.of(
                new Author("Author_1"),
                new Author("Author_2"),
                new Author("Author_3")
            )
        );

        var genres = genreRepository.saveAll(
                List.of(
                        new Genre("Genre_1"),
                        new Genre("Genre_2"),
                        new Genre("Genre_3")
                )
        );

        var books = bookRepository.saveAll(
           List.of(
                   new Book("Book_1", authors.get(0), genres.get(0)),
                   new Book("Book_2", authors.get(1), genres.get(1)),
                   new Book("Book_3", authors.get(2), genres.get(2))
           )
        );

        commentRepository.save(new Comment("Comment_1", books.get(0)));
        commentRepository.save(new Comment("Comment_12", books.get(0)));
        commentRepository.save(new Comment("Comment_2", books.get(1)));
    }

}
