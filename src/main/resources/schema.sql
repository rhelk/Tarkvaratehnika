create sequence users_sequence start with 1;
create sequence property_sequence start with 1;

create table users (
 user_id BIGINT NOT NULL PRIMARY KEY,
 first_name VARCHAR(50),
 last_name VARCHAR(50),
 email VARCHAR(50),
 password VARCHAR(255)
);

create table property (
 property_id bigint NOT NULL PRIMARY KEY,
 description CLOB,
 address VARCHAR(255),
 pic_url VARCHAR(255),
 room_count SMALLINT,
 bed_count SMALLINT,
 owner_id BIGINT,
 price BIGINT,
 FOREIGN KEY(owner_id) REFERENCES users
);
