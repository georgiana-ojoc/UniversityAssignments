SET SERVEROUTPUT ON;

DECLARE
    v_cursor_id INTEGER;
    v_ok        INTEGER;
BEGIN
    v_cursor_id := DBMS_SQL.OPEN_CURSOR;
    DBMS_SQL.PARSE(v_cursor_id, 'CREATE TABLE test (id NUMBER(2,2), val VARCHAR2(30))', DBMS_SQL.NATIVE);
    v_ok := DBMS_SQL.EXECUTE(v_cursor_id);
    DBMS_SQL.PARSE(v_cursor_id, 'DROP TABLE test', DBMS_SQL.NATIVE);
    v_ok := DBMS_SQL.EXECUTE(v_cursor_id);
    DBMS_SQL.CLOSE_CURSOR(v_cursor_id);
END;
/
CREATE OR REPLACE PROCEDURE afiseaza_profesori(p_camp VARCHAR2) AS
    v_cursor_id     INTEGER;
    v_ok            INTEGER;
    v_id_prof       INTEGER;
    v_nume_prof     VARCHAR2(15);
    v_prenume_prof  VARCHAR2(30);
BEGIN
    v_cursor_id := DBMS_SQL.OPEN_CURSOR;
    DBMS_SQL.PARSE(v_cursor_id, 'SELECT id, nume, prenume FROM profesori ORDER BY ' || p_camp, DBMS_SQL.NATIVE);
    DBMS_SQL.DEFINE_COLUMN(v_cursor_id, 1, v_id_prof);
    DBMS_SQL.DEFINE_COLUMN(v_cursor_id, 2, v_nume_prof,15);
    DBMS_SQL.DEFINE_COLUMN(v_cursor_id, 3, v_prenume_prof,30);
    v_ok := DBMS_SQL.EXECUTE(v_cursor_id);
    LOOP
        IF DBMS_SQL.FETCH_ROWS(v_cursor_id) > 0 THEN
            DBMS_SQL.COLUMN_VALUE(v_cursor_id, 1, v_id_prof);
            DBMS_SQL.COLUMN_VALUE(v_cursor_id, 2, v_nume_prof);
            DBMS_SQL.COLUMN_VALUE(v_cursor_id, 3, v_prenume_prof);
            DBMS_OUTPUT.PUT_LINE(v_id_prof || '   ' || v_nume_prof || '   ' || v_prenume_prof);
        ELSE
            EXIT;
        END IF;
    END LOOP;
    DBMS_SQL.CLOSE_CURSOR(v_cursor_id);
END;
/
BEGIN
    afiseaza_profesori('nume');
END;
/
CREATE OR REPLACE FUNCTION get_type(p_record_table DBMS_SQL.DESC_TAB, p_column INTEGER) RETURN VARCHAR2 AS
    v_column_type   VARCHAR2(200);
    v_precision     VARCHAR2(40);
BEGIN
    CASE (p_record_table(p_column).col_type)
        WHEN 1 THEN
            v_column_type := 'VARCHAR2';
            v_precision := '(' || p_record_table(p_column).col_max_len || ')';
        WHEN 2 THEN
            v_column_type := 'NUMBER';
            v_precision := '(' || p_record_table(p_column).col_precision || ',' || p_record_table(p_column).col_scale || ')';
        WHEN 8 THEN
            v_column_type := 'LONG';
            v_precision := '';
        WHEN 11 THEN
            v_column_type := 'ROWID';
            v_precision := '';
        WHEN 12 THEN
            v_column_type := 'DATE';
            v_precision := '';
        WHEN 23 THEN
            v_column_type := 'RAW';
            v_precision := '(' || p_record_table(p_column).col_max_len || ')';
        WHEN 96 THEN
            v_column_type := 'CHAR';
            v_precision := '(' || p_record_table(p_column).col_max_len || ')';
        WHEN 100 THEN
            v_column_type := 'BINARY_FLOAT';
            v_precision := '';
        WHEN 101 THEN
            v_column_type := 'BINARY_DOUBLE';
            v_precision := '';
        WHEN 109 THEN
            v_column_type := 'URITYPE';
            v_precision := '';
        WHEN 112 THEN
            v_column_type := 'CLOB';
            v_precision := '';
        WHEN 113 THEN
            v_column_type := 'BLOB';
            v_precision := '';
        WHEN 114 THEN
            v_column_type := 'BFILE';
            v_precision := '';
        WHEN 180 THEN
            v_column_type := 'TIMESTAMP';
            v_precision :='(' || p_record_table(p_column).col_scale || ')';
        WHEN 181 THEN
            v_column_type := 'TIMESTAMP' || '(' || p_record_table(p_column).col_scale || ') ' || 'WITH TIME ZONE';
            v_precision := '';
        WHEN 231 THEN
            v_column_type := 'TIMESTAMP' || '(' || p_record_table(p_column).col_scale || ') ' || 'WITH LOCAL TIME ZONE';
            v_precision := '';
    END CASE;
    RETURN v_column_type || v_precision;
END;
/
DECLARE
    v_cursor_id     NUMBER;
    v_ok            NUMBER;
    v_columns       NUMBER;
    v_record_table  DBMS_SQL.DESC_TAB;
    v_column        NUMBER;
BEGIN
    v_cursor_id  := DBMS_SQL.OPEN_CURSOR;
    DBMS_SQL.PARSE(v_cursor_id , 'SELECT * FROM studenti', DBMS_SQL.NATIVE);
    v_ok := DBMS_SQL.EXECUTE(v_cursor_id);
    DBMS_SQL.DESCRIBE_COLUMNS(v_cursor_id, v_columns, v_record_table);
    v_column := v_record_table.first;
    IF (v_column IS NOT NULL) THEN
        LOOP
            DBMS_OUTPUT.PUT_LINE(v_record_table(v_column).col_name || ' ' || get_type(v_record_table,v_column));
            v_column := v_record_table.next(v_column);
            EXIT WHEN (v_column IS NULL);
        END LOOP;
    END IF;
    DBMS_SQL.CLOSE_CURSOR(v_cursor_id);
END;
