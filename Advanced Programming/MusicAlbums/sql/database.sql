CREATE USER 'dba'@'localhost' IDENTIFIED BY 'sql';
GRANT ALL PRIVILEGES ON *.* TO 'dba'@'localhost';
CREATE TABLE artists
    (id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    country VARCHAR(100),
    UNIQUE (name, country));
CREATE TABLE albums
    (id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    id_artist INT NOT NULL,
    release_year INT,
    FOREIGN KEY (id_artist) REFERENCES artists (id) ON DELETE RESTRICT,
    UNIQUE (name, id_artist));
CREATE TABLE charts
    (id INT AUTO_INCREMENT PRIMARY KEY,
    id_chart INT NOT NULL,
    id_album INT NOT NULL,
    place INT NOT NULL,
    FOREIGN KEY (id_album) REFERENCES albums (id) ON DELETE RESTRICT,
    UNIQUE (id, id_album));
