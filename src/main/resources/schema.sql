create sequence users_sequence start with 1;
create sequence property_sequence start with 1;

create table users (
 user_id BIGINT NOT NULL PRIMARY KEY,
 first_name VARCHAR(50),
 last_name VARCHAR(50),
 username VARCHAR(50) NOT NULL UNIQUE,
 password VARCHAR(255) NOT NULL,
 enabled BOOLEAN DEFAULT TRUE,
);

create table property (
 property_id bigint NOT NULL PRIMARY KEY,
 title VARCHAR(255),
 description CLOB,
 address VARCHAR(255),
 county VARCHAR(255),
 municipality VARCHAR(255),
 settlement VARCHAR(255),
 street VARCHAR(255),
 pic_url VARCHAR(255),
 room_count SMALLINT,
 bed_count SMALLINT,
 owner_id BIGINT,
 price BIGINT,
 visibility VARCHAR(10) DEFAULT 'VISIBLE',
 FOREIGN KEY(owner_id) REFERENCES users
);

CREATE TABLE authorities (
  username VARCHAR(50) NOT NULL,
  authority VARCHAR(50) DEFAULT 'ROLE_USER',
  FOREIGN KEY (username)
    REFERENCES users (username) ON DELETE CASCADE
);
