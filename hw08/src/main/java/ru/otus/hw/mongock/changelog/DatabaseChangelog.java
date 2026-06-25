package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Arrays;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "root", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAllAuthor", author = "root")
    public void insertAllAuthor(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("author");
        var author1 = new Document().append("_id", 1).append("fullName", "Author_1");
        var author2 = new Document().append("_id", 2).append("fullName", "Author_2");
        var author3 = new Document().append("_id", 3).append("fullName", "Author_3");
        myCollection.insertMany(Arrays.asList(author1, author2, author3));
    }

    @ChangeSet(order = "003", id = "insertAllGenre", author = "root")
    public void insertAllGenre(GenreRepository repository) {
        repository.save(new Genre(1, "Genre_1"));
        repository.save(new Genre(2, "Genre_2"));
        repository.save(new Genre(3, "Genre_3"));
    }

    @ChangeSet(order = "004", id = "insertAllBook", author = "root")
    public void insertAllGenre(
            BookRepository repository, AuthorRepository authorRepository, GenreRepository genreRepository) {
        repository.save(new Book(1, "Book_1", authorRepository.findById(1L).get(), genreRepository.findById(1L).get()));
        repository.save(new Book(2, "Book_2", authorRepository.findById(2L).get(), genreRepository.findById(2L).get()));
        repository.save(new Book(3, "Book_3", authorRepository.findById(3L).get(), genreRepository.findById(3L).get()));
    }

    @ChangeSet(order = "005", id = "insertAllComment", author = "root")
    public void insertAllComment(
            CommentRepository repository, BookRepository bookRepository) {
        repository.save(new Comment(1, "Comment_1", bookRepository.findById(1L).get()));
        repository.save(new Comment(2, "Comment_12", bookRepository.findById(1L).get()));
        repository.save(new Comment(3, "Comment_2", bookRepository.findById(2L).get()));
    }
}
