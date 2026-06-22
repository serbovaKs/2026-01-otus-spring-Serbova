insert into author(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genre(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3');

insert into book(title, author_id, genre_id)
values ('BookTitle_1', 1, 1), ('BookTitle_2', 2, 2), ('BookTitle_3', 3, 3);

insert into comment(text, book_id)
values ('test', 1);