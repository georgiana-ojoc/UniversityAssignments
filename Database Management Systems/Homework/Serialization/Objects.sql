BEGIN
    drop_if_exists('objects', 'table');
END;
/
CREATE TABLE objects
(
    identifier NUMBER       NOT NULL PRIMARY KEY,
    name       VARCHAR2(30) NOT NULL,
    object     BLOB         NOT NULL
);
