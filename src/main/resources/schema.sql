-- The table name is plural because user is a reserved word

CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(50) UNIQUE NOT NULL,
    login VARCHAR(50) NOT NULL,
    name VARCHAR(50) NOT NULL,
    birthday DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS mpa (
    mpa_id INTEGER AUTO_INCREMENT PRIMARY KEY ,
    name VARCHAR(10) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS genre (
    genre_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(30) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS director (
    director_id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS film (
    film_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(200) NOT NULL,
    release_date DATE NOT NULL,
    duration INTEGER NOT NULL,
    mpa_id INTEGER NOT NULL,
    FOREIGN KEY (mpa_id) REFERENCES mpa (mpa_id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS film_genre (
    film_id INTEGER NOT NULL,
    genre_id INTEGER NOT NULL,
    FOREIGN KEY (film_id) REFERENCES film (film_id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genre (genre_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS film_director (
    film_id INTEGER NOT NULL,
    director_id INTEGER NOT NULL,
    FOREIGN KEY (film_id) REFERENCES film (film_id) ON DELETE CASCADE,
    FOREIGN KEY (director_id) REFERENCES director (director_id) ON DELETE CASCADE
);

-- The table name is plural because like is a reserved word
CREATE TABLE IF NOT EXISTS likes (
    film_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    FOREIGN KEY (film_id) REFERENCES film (film_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS friendship (
    user_id   INTEGER NOT NULL,
    friend_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS event (
    event_id   int PRIMARY KEY AUTO_INCREMENT,
    timestamp  long NOT NULL,
    user_id    int NOT NULL,
    event_type ENUM('LIKE', 'REVIEW', 'FRIEND') NOT NULL,
    operation ENUM('REMOVE', 'ADD', 'UPDATE')   NOT NULL,
    entity_id  int NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS review (
    review_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(256) NOT NULL,
    is_positive BOOLEAN NOT NULL,
    user_id INTEGER NOT NULL,
    film_id INTEGER NOT NULL,
    useful INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (film_id) REFERENCES film (film_id) ON DELETE CASCADE
);
