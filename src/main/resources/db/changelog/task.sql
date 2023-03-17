CREATE TABLE task
(
    id      SERIAL PRIMARY KEY,
    chat_id BIGINT    NOT NULL,
    message TEXT      NOT NULL,
    time    TIMESTAMP NOT NULL
)