
CREATE TABLE IF NOT EXISTS user
(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(150) UNIQUE NOT NULL,
    username VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(150) NOT NULL,
    role ENUM('ADMIN', 'USER') NOT NULL,
    first_name VARCHAR(150) NOT NULL,
    last_name VARCHAR(150) NOT NULL,
    phone_number VARCHAR(50),
    image_url VARCHAR(300),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS url
(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    full_url VARCHAR(3500) NOT NULL,
    short_url_path VARCHAR(100) UNIQUE NOT NULL,
    clicks BIGINT UNSIGNED DEFAULT 0,
    user_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);
