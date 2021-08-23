CREATE SEQUENCE IF NOT EXISTS chats_id_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE IF NOT EXISTS chats
(
    id BIGINT NOT NULL UNIQUE,
    chat_type VARCHAR(80) NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    chat_name VARCHAR(80),
    chat_picture_id BIGINT,
    PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS users_id_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE IF NOT EXISTS users
(
    id BIGINT NOT NULL UNIQUE,
    username VARCHAR(80) NOT NULL UNIQUE,
    password VARCHAR(80) NOT NULL,
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL,
    bio VARCHAR(256),
    phone_number VARCHAR(80) NOT NULL UNIQUE,
    email VARCHAR(80) UNIQUE,
    profile_picture_id BIGINT,
    account_type VARCHAR(80) NOT NULL,
    account_creation_date TIMESTAMP NOT NULL,
    last_login_date TIMESTAMP,
    last_online_date TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS chat_members_id_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE IF NOT EXISTS chat_members
(
    id BIGINT NOT NULL UNIQUE DEFAULT nextval('chat_members_id_seq'),
    chat_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (chat_id) REFERENCES chats(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE SEQUENCE IF NOT EXISTS messages_id_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE IF NOT EXISTS messages
(
    id BIGINT NOT NULL UNIQUE,
    chat_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    text VARCHAR(1000) NOT NULL,
    attachment_id BIGINT,
    creation_date TIMESTAMP NOT NULL,
    read_date TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (chat_id) REFERENCES chats(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);