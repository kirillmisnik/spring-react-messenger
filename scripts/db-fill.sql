INSERT INTO users VALUES
(
    nextval('users_id_seq'),
    'martin_g', '$2a$10$1oZ2/wkHV4hbqxJj1ye3eO5Rf2Bong9lS8Fub7Bkh8ZRLPuVhdsHu',
    'George', 'Martin',
    'I am a sample user.',
    '+7 (116) 308-66-15', 'fifth.beatle@gmail.com',
    null,
    'ADMIN',
    '1965-06-29 02:17:10', '2021-07-28 04:45:49', '2021-07-30 13:07:36'
),
(
    nextval('users_id_seq'),
    'mccartney', '$2a$10$1oZ2/wkHV4hbqxJj1ye3eO5Rf2Bong9lS8Fub7Bkh8ZRLPuVhdsHu',
    'Paul', 'McCartney',
    'I am a sample user.',
    '+7 (985) 123-45-67', 'mccartney@gmail.com',
    null,
    'USER',
    '1957-07-06 10:23:54', '2021-07-09 18:54:11', '2021-07-11 10:23:42'
),
(
    nextval('users_id_seq'),
    'lennon_john', '$2a$10$1oZ2/wkHV4hbqxJj1ye3eO5Rf2Bong9lS8Fub7Bkh8ZRLPuVhdsHu',
    'John', 'Lennon',
    'I am a sample user.',
    '+7 (183) 707-20-61', 'lennon.john@gmail.com',
    null,
    'USER',
    '1957-07-06 12:41:35', '2021-07-12 21:25:32', '2021-07-13 08:37:47'
),
(
    nextval('users_id_seq'),
    'starr', '$2a$10$1oZ2/wkHV4hbqxJj1ye3eO5Rf2Bong9lS8Fub7Bkh8ZRLPuVhdsHu',
    'Ringo', 'Starr',
    'I am a sample user.',
    '+7 (922) 815-93-45', 'starr@gmail.com',
    null,
    'USER',
    '1960-07-02 21:31:41', '2021-07-16 06:39:01', '2021-07-18 01:00:46'
),
(
    nextval('users_id_seq'),
    'travelling_wilbury', '$2a$10$1oZ2/wkHV4hbqxJj1ye3eO5Rf2Bong9lS8Fub7Bkh8ZRLPuVhdsHu',
    'George', 'Harrison',
    'I am a sample user.',
    '+7 (364) 995-33-54', 'g.harrison@gmail.com',
    null,
    'USER',
    '1960-06-25 00:18:39', '2021-07-21 03:40:57', '2021-07-25 09:58:37'
);

INSERT INTO chats VALUES
(nextval('chats_id_seq'), 'PERSONAL', '1960-06-28 10:23:54', null, null),
(nextval('chats_id_seq'), 'PERSONAL', '1960-08-21 11:20:45', null, null),
(nextval('chats_id_seq'), 'GROUP', '1960-06-28 10:23:54', 'The Beatles', null),
(nextval('chats_id_seq'), 'PERSONAL', '1960-06-28 10:23:54', null, null);

INSERT INTO chat_members VALUES
(nextval('chat_members_id_seq'), 1, 1), (nextval('chat_members_id_seq'), 1, 2),
(nextval('chat_members_id_seq'), 2, 1), (nextval('chat_members_id_seq'), 2, 3),
(nextval('chat_members_id_seq'), 3, 1), (nextval('chat_members_id_seq'), 3, 2), (nextval('chat_members_id_seq'), 3, 3),
(nextval('chat_members_id_seq'), 4, 2), (nextval('chat_members_id_seq'), 4, 3);

INSERT INTO messages VALUES
(nextval('messages_id_seq'), 1, 2, 'Hi, George!', null, '1967-06-28 10:23:54', '1967-06-28 10:23:54'),
(nextval('messages_id_seq'), 1, 1, 'Hello Paul', null, '1967-06-28 10:23:59', '1967-06-28 10:23:59'),
(nextval('messages_id_seq'), 1, 1, 'How are you?', null, '1967-06-28 10:24:01', '1967-06-28 10:24:01'),
(nextval('messages_id_seq'), 1, 2, 'Fine, thanks!', null, '1967-06-28 10:24:54', '1967-06-28 10:24:54'),
(nextval('messages_id_seq'), 1, 2, 'What about our new album?', null, '1967-06-28 10:25:00', null),
(nextval('messages_id_seq'), 1, 1, 'It is gonna be out on may 26-th', null, '1967-06-28 10:25:32', null),
(nextval('messages_id_seq'), 3, 1, 'Hi!', null, '2021-06-28 10:24:54', '1962-06-28 10:24:54'),
(nextval('messages_id_seq'), 3, 1, 'I made chat for all of us!', null, '1962-06-28 10:25:00', null),
(nextval('messages_id_seq'), 3, 3, 'Need to invite Ringo..', null, '1962-06-28 10:29:00', null);