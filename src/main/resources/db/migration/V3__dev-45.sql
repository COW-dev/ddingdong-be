create table file_meta_data
(
    id          bigint auto_increment
        primary key,
    created_at  timestamp    null,
    updated_at  timestamp    null,
    file_domain varchar(255) null,
    file_id     varchar(255) not null,
    file_name   varchar(255) not null
);
