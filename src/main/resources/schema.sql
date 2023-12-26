create table customers (
    id bigint auto_increment primary key,
    email varchar(255) not null unique,
    name varchar(255) not null,
    password varchar(255) not null
);