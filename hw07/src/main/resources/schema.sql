create table author (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);

create table genre (
    id bigserial,
    name varchar(255),
    primary key (id)
);

create table book (
    id bigserial,
    title varchar(255),
    author_id bigint references author(id) on delete cascade,
    genre_id bigint references genre(id) on delete cascade,
    primary key (id)
);

create table comment (
    id bigserial,
    book_id bigint references book(id) on delete cascade,
    text varchar(255),
    primary key (id)
);