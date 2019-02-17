create sequence hibernate_sequence start with 1;

create table property(
 id bigint primary key,
 text VARCHAR(255),
 rooms SMALLINT
);

create table users(
 id bigint primary key,
 first_name VARCHAR(50),
 last_name VARCHAR(50),
 email VARCHAR(50),
 password VARCHAR(255)
);