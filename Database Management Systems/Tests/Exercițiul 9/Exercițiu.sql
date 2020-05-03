SET SERVEROUTPUT ON;

CREATE OR REPLACE FUNCTION get_type(p_record_table IN DBMS_SQL.DESC_TAB, p_column_number IN NUMBER) RETURN VARCHAR2 AS
BEGIN
    CASE (p_record_table(p_column_number).col_type)
        WHEN 1 THEN
            RETURN 'VARCHAR2' || '(' || p_record_table(p_column_number).col_max_len || ')';
        WHEN 2 THEN
            RETURN 'NUMBER' || '(' || p_record_table(p_column_number).col_precision || ','
                       || p_record_table(p_column_number).col_scale || ')';
        WHEN 8 THEN
            RETURN 'LONG';
        WHEN 11 THEN
            RETURN 'ROWID';
        WHEN 12 THEN
            RETURN 'DATE';
        WHEN 23 THEN
            RETURN 'RAW' || '(' || p_record_table(p_column_number).col_max_len || ')';
        WHEN 96 THEN
            RETURN 'CHAR' || '(' || p_record_table(p_column_number).col_max_len || ')';
        WHEN 100 THEN
            RETURN 'BINARY_FLOAT';
        WHEN 101 THEN
            RETURN 'BINARY_DOUBLE';
        WHEN 112 THEN
            RETURN 'CLOB';
        WHEN 113 THEN
            RETURN 'BLOB';
        WHEN 114 THEN
            RETURN 'BFILE';
        WHEN 180 THEN
            RETURN 'TIMESTAMP' || '(' || p_record_table(p_column_number).col_scale || ')';
        WHEN 181 THEN
            RETURN 'TIMESTAMP' || '(' || p_record_table(p_column_number).col_scale || ') ' || 'WITH TIME ZONE';
        WHEN 231 THEN
            RETURN 'TIMESTAMP' || '(' || p_record_table(p_column_number).col_scale || ') ' || 'WITH LOCAL TIME ZONE';
        ELSE
            RETURN 'UNKNOWN';
    END CASE;
END;
/
CREATE OR REPLACE PROCEDURE creeaza_catalog(p_id_curs cursuri.id%TYPE) AS
    v_cursor            NUMBER;
    v_cursor_insert     NUMBER;
    v_return_value      NUMBER;
    v_columns_number    NUMBER;
    v_record_table      DBMS_SQL.DESC_TAB;
    v_curs              cursuri.titlu_curs%TYPE;
BEGIN
    v_cursor := DBMS_SQL.OPEN_CURSOR;
    -- Determin tipul fiecărei coloane din catalog.
    DBMS_SQL.PARSE(v_cursor, 'SELECT nr_matricol, nume, prenume, titlu_curs, valoare, data_notare ' ||
                                'FROM studenti JOIN note ON studenti.id = note.id_student ' ||
                                'JOIN cursuri ON note.id_curs = cursuri.id ' ||
                                'WHERE ROWNUM < 2', DBMS_SQL.NATIVE);
    v_return_value := DBMS_SQL.EXECUTE(v_cursor);
    DBMS_SQL.DESCRIBE_COLUMNS(v_cursor, v_columns_number, v_record_table);

    -- Determin numele cursului cu identificatorul specificat.
    DBMS_SQL.PARSE(v_cursor, 'SELECT titlu_curs FROM cursuri WHERE id = :id', DBMS_SQL.NATIVE);
    DBMS_SQL.BIND_VARIABLE(v_cursor, 'id', p_id_curs);
    DBMS_SQL.DEFINE_COLUMN(v_cursor, 1, v_curs, v_record_table(4).col_max_len);
    v_return_value := DBMS_SQL.EXECUTE_AND_FETCH(v_cursor);
    DBMS_SQL.COLUMN_VALUE(v_cursor, 1, v_curs);

    -- Creez catalogul.
    v_curs := REGEXP_REPLACE(v_curs, '[^a-zA-Z0-9]', '');
    drop_if_exists(v_curs, 'table');
    DBMS_SQL.PARSE(v_cursor, 'CREATE TABLE ' || v_curs ||
                             ' (nr_matricol ' || get_type(v_record_table, 1) ||
                             ', nume ' || get_type(v_record_table, 2) ||
                             ', prenume ' || get_type(v_record_table, 3) ||
                             ', valoare ' || get_type(v_record_table, 5) ||
                             ', data_notare ' || get_type(v_record_table, 6) || ')',
        DBMS_SQL.NATIVE);
    v_return_value := DBMS_SQL.EXECUTE(v_cursor);

    -- Inserez informațiile în catalog.
    v_cursor_insert := DBMS_SQL.OPEN_CURSOR;
    FOR v_record IN (SELECT nr_matricol, nume, prenume, valoare, data_notare
                        FROM studenti JOIN note ON studenti.id = note.id_student
                        JOIN cursuri ON note.id_curs = cursuri.id
                        WHERE cursuri.id = p_id_curs) LOOP
        DBMS_SQL.PARSE(v_cursor_insert, 'INSERT INTO ' || v_curs || ' VALUES ' ||
                                        '(:nr_matricol, :nume, :prenume, :valoare, :data_notare)',
            DBMS_SQL.NATIVE);
        DBMS_SQL.BIND_VARIABLE(v_cursor_insert, 'nr_matricol', v_record.nr_matricol);
        DBMS_SQL.BIND_VARIABLE(v_cursor_insert, 'nume', v_record.nume);
        DBMS_SQL.BIND_VARIABLE(v_cursor_insert, 'prenume', v_record.prenume);
        DBMS_SQL.BIND_VARIABLE(v_cursor_insert, 'valoare', v_record.valoare);
        DBMS_SQL.BIND_VARIABLE(v_cursor_insert, 'data_notare', v_record.data_notare);
        v_return_value := DBMS_SQL.EXECUTE(v_cursor_insert);
    END LOOP;

    DBMS_SQL.CLOSE_CURSOR(v_cursor);
END;
/
DECLARE
    v_id_curs cursuri.id%TYPE;
BEGIN
    SELECT MAX(id) INTO v_id_curs FROM cursuri;
    creeaza_catalog(v_id_curs);
END;
