-- liquibase formatted sql

-- changeSet Alex: 1
CREATE TABLE notification_task
(
    id      SERIAL PRIMARY KEY,
    chat_id BIGINT,
    message TEXT,
    time    TIMESTAMP
);