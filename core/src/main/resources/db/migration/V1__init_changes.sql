CREATE TABLE IF NOT EXISTS images (
    id          BIGSERIAL PRIMARY KEY,
    file_name   TEXT,
    size        BIGINT,
    upload_date TIMESTAMP WITH TIME ZONE,
    user_id     BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    url         TEXT
);

CREATE TABLE IF NOT EXISTS users (
    id            BIGSERIAL PRIMARY KEY,
    username      TEXT NOT NULL UNIQUE,
    email         TEXT NOT NULL UNIQUE,
    password      TEXT NOT NULL,
    role          TEXT NOT NULL,
    blocked_at    TIMESTAMP WITH TIME ZONE
);


CREATE TABLE IF NOT EXISTS user_images (
    user_id   BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    image_url TEXT,
    PRIMARY KEY (user_id, image_url)
);
