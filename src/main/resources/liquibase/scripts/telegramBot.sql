--liquibase formatted sql

--changeset KorolchukVladislav:3
CREATE TABLE Notifications
(
    id bigserial,
    chatID INTEGER,
    messageText VARCHAR(255),
    time TIMESTAMP
);
