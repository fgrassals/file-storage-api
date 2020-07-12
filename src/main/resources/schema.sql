create table users
(
    id               bigint         not null primary key auto_increment,
    username         varchar(20) not null unique,
    password         varchar(64) not null,
    created_at       datetime    not null default current_timestamp,
    last_modified_at datetime on update current_timestamp
);
