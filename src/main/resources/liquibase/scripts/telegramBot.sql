--liquibase formatted sql

--changeset KorolchukVladislav:5
CREATE TABLE Notifications1
(
    id bigserial,
    chatID INTEGER,
    messageText VARCHAR(255),
    time TIMESTAMP
);
