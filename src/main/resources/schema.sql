create table users
(
    id               bigint      not null primary key auto_increment,
    username         varchar(20) not null unique,
    password         varchar(64) not null,
    created_at       datetime    not null default current_timestamp,
    last_modified_at datetime on update current_timestamp
);

create table files
(
    id               bigint       not null primary key auto_increment,
    filename         varchar(100) not null,
    content_type     varchar(50)  not null,
    user_id          int          not null,
    created_at datetime    not null default current_timestamp,

    foreign key (user_id) references users (id),
    unique (filename, user_id)
);

create table file_versions
(
    id         bigint      not null primary key auto_increment,
    uuid       varchar(36) not null unique,
    file_id    bigint      not null,
    size       int         not null,
    content    longblob,
    created_at datetime    not null default current_timestamp,

    foreign key (file_id) references files (id)
);

create index ix_file_versions_created_at on file_versions (file_id, created_at desc);

/*
 This view selects files with their versions, and ranks the versions
 by their creation date, so the newest file version will have a rank of 1
  and the oldest will have a rank of N
*/
create view user_files_view as (
    with files_ranked as (
        select f.id,
               f.filename,
               f.content_type,
               fv.uuid version,
               fv.content,
               fv.size size_in_bytes,
               f.created_at,
               fv.created_at last_modified_at,
               f.user_id,
               rank() over (partition by f.user_id, f.filename order by fv.created_at desc) date_created_rank
        from files f
            inner join file_versions fv on f.id = fv.file_id
    )
    select * from files_ranked
);
