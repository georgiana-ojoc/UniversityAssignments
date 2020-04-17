SET SERVEROUTPUT ON;

GRANT EXECUTE ON UTL_FILE TO STUDENT; -- SYSDBA
/
GRANT CREATE ANY DIRECTORY TO STUDENT; -- SYSDBA
/
CREATE OR REPLACE DIRECTORY Exercitiul_8 AS 'D:\Anul II\Semestrul II\Practica SGBD\Teste\Exercitiul 8';
/
GRANT READ, WRITE ON DIRECTORY EXERCITIUL_8 TO STUDENT; -- SYSDBA
/
CREATE OR REPLACE PROCEDURE exporta_note AS
    v_fisier    UTL_FILE.FILE_TYPE;
    v_count     NUMBER DEFAULT 0;
BEGIN
    v_fisier := UTL_FILE.FOPEN('EXERCITIUL_8', 'note.csv', 'W');
    FOR intrare IN (SELECT * FROM note) LOOP
        UTL_FILE.PUT_LINE(v_fisier, intrare.id || ',' || intrare.id_student || ',' || intrare.id_curs || ',' ||
                                    intrare.valoare || ',' || intrare.data_notare || ',' ||
                                    intrare.created_at || ',' || intrare.updated_at);
        v_count := v_count + 1;
    END LOOP;
    DBMS_OUTPUT.PUT_LINE(v_count || ' note au fost exportate in fisierul "note.csv".');
    UTL_FILE.FCLOSE(v_fisier);
EXCEPTION
    WHEN UTL_FILE.INVALID_FILEHANDLE
        THEN DBMS_OUTPUT.PUT_LINE('Fisierul nu a fost deschis.');
    WHEN UTL_FILE.INVALID_OPERATION
        THEN DBMS_OUTPUT.PUT_LINE('Fisierul este deja deschis din alta parte.');
END exporta_note;
/
CREATE OR REPLACE PROCEDURE importa_note AS
    v_fisier        UTL_FILE.FILE_TYPE;
    v_intrare       VARCHAR2(1024);
    v_id            note.id%TYPE;
    v_id_student    note.id_student%TYPE;
    v_id_curs       note.id_curs%TYPE;
    v_valoare       note.valoare%TYPE;
    v_data_notare   note.data_notare%TYPE;
    v_created_at    note.created_at%TYPE;
    v_updated_at    note.updated_at%TYPE;
    v_count         NUMBER DEFAULT 0;
BEGIN
    v_fisier := UTL_FILE.FOPEN('EXERCITIUL_8', 'note.csv', 'R');
    LOOP
        UTL_FILE.GET_LINE(v_fisier, v_intrare);
        v_id := REGEXP_SUBSTR(v_intrare, '[^,]+', 1, 1);
        v_id_student := REGEXP_SUBSTR(v_intrare, '[^,]+', 1, 2);
        v_id_curs := REGEXP_SUBSTR(v_intrare, '[^,]+', 1, 3);
        v_valoare := REGEXP_SUBSTR(v_intrare, '[^,]+', 1, 4);
        v_data_notare := REGEXP_SUBSTR(v_intrare, '[^,]+', 1, 5);
        v_created_at := REGEXP_SUBSTR(v_intrare, '[^,]+', 1, 6);
        v_updated_at := REGEXP_SUBSTR(v_intrare, '[^,]+', 1, 7);
        INSERT INTO note VALUES (v_id, v_id_student, v_id_curs, v_valoare, v_data_notare,
                                 v_created_at, v_updated_at);
        v_count := v_count + 1;
    END LOOP;
EXCEPTION
    WHEN NO_DATA_FOUND
        THEN
            UTL_FILE.FCLOSE(v_fisier);
            DBMS_OUTPUT.PUT_LINE(v_count || ' note au fost importate in fisierul "note.csv".');
END importa_note;
/
BEGIN
    exporta_note();
    DELETE FROM note WHERE 1 = 1;
    importa_note();
END;
/
SELECT * FROM note;
