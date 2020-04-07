SET SERVEROUTPUT ON;

CREATE OR REPLACE PROCEDURE drop_if_exists(p_name VARCHAR2, p_type VARCHAR2) AS
    v_count NUMBER;
BEGIN
    CASE
        WHEN LOWER(p_type) = 'table' THEN
            SELECT COUNT(*) INTO v_count FROM user_tables WHERE UPPER(table_name) = UPPER(p_name);
        WHEN LOWER(p_type) = 'constraint' THEN
            SELECT COUNT(*) INTO v_count FROM user_constraints WHERE UPPER(constraint_name) = UPPER(p_name);
        WHEN LOWER(p_type) = 'index' THEN
            SELECT COUNT(*) INTO v_count FROM user_indexes WHERE UPPER(index_name) = UPPER(p_name);
        WHEN LOWER(p_type) = 'procedure' THEN
            SELECT COUNT(*) INTO v_count FROM user_procedures WHERE UPPER(object_name) = UPPER(p_name);
        WHEN LOWER(p_type) = 'function' THEN
            SELECT COUNT(*) INTO v_count FROM user_procedures WHERE UPPER(object_name) = UPPER(p_name);
    END CASE;
    IF v_count = 1 THEN
        CASE
            WHEN LOWER(p_type) = 'table' THEN EXECUTE IMMEDIATE 'DROP TABLE ' || UPPER(p_name);
            WHEN LOWER(p_type) = 'constraint' THEN EXECUTE IMMEDIATE 'DROP CONSTRAINT ' || UPPER(p_name);
            WHEN LOWER(LOWER(p_type)) = 'index' THEN EXECUTE IMMEDIATE 'DROP INDEX ' || UPPER(UPPER(p_name));
            WHEN LOWER(p_type) = 'procedure' THEN EXECUTE IMMEDIATE 'DROP PROCEDURE ' || UPPER(p_name);
            WHEN LOWER(p_type) = 'function' THEN EXECUTE IMMEDIATE 'DROP FUNCTION ' || UPPER(p_name);
        END CASE;
    END IF;
END drop_if_exists;
/
BEGIN
    drop_if_exists('nota_unica', 'index');
END;
/
CREATE UNIQUE INDEX nota_unica ON note(id_student, id_curs);
/
CREATE OR REPLACE PROCEDURE adauga_nota_exista
    (p_id_student studenti.id%TYPE, p_titlu_curs cursuri.titlu_curs%TYPE, p_valoare note.valoare%TYPE) AS
    v_count_studenti    NUMBER;
    v_count_cursuri     NUMBER;
    v_count_note        NUMBER;
    v_id_curs           cursuri.id%TYPE;
    v_id_nota           note.id%TYPE;
BEGIN
    /*
    SELECT COUNT(*) INTO v_count_studenti FROM studenti WHERE id = p_id_student;
    IF v_count_studenti = 0 THEN
        DBMS_OUTPUT.PUT_LINE('Studentul cu ID-ul ' || p_id_student || ' nu exista in baza de date.');
    END IF;
    SELECT COUNT(*) INTO v_count_cursuri FROM cursuri WHERE UPPER(titlu_curs) = UPPER(p_titlu_curs);
    IF v_count_cursuri = 0 THEN
        DBMS_OUTPUT.PUT_LINE('Cursul ' || p_titlu_curs || ' nu exista in baza de date.');
    END IF;
    */
    SELECT id INTO v_id_curs FROM cursuri WHERE UPPER(titlu_curs) = UPPER(p_titlu_curs);
    SELECT COUNT(*) INTO v_count_note FROM note WHERE id_student = p_id_student AND id_curs = v_id_curs;
    IF v_count_note = 0 THEN
        SELECT MAX(id) + 1 INTO v_id_nota FROM note;
        INSERT INTO note VALUES (v_id_curs, p_id_student, v_id_curs, p_valoare, SYSDATE, SYSDATE, SYSDATE);
    /*
    ELSE
        DBMS_OUTPUT.PUT_LINE('Studentul cu ID-ul ' || p_id_student ||
                         ' are deja nota la materia ' || p_titlu_curs || '.');
    */
    END IF;
END adauga_nota_exista;
/
CREATE OR REPLACE PROCEDURE adauga_nota_exceptie
    (p_id_student studenti.id%TYPE, p_titlu_curs cursuri.titlu_curs%TYPE, p_valoare note.valoare%TYPE) AS
    student_inexistent  EXCEPTION;
    PRAGMA EXCEPTION_INIT(student_inexistent, -20001);
    curs_inexistent  EXCEPTION;
    PRAGMA EXCEPTION_INIT(curs_inexistent, -20002);
    v_count_studenti    NUMBER;
    v_count_cursuri     NUMBER;
    v_id_curs           cursuri.id%TYPE;
    v_id_nota           note.id%TYPE;
BEGIN
    /*
    SELECT COUNT(*) INTO v_count_studenti FROM studenti WHERE id = p_id_student;
    IF v_count_studenti = 0 THEN
        RAISE student_inexistent;
    END IF;
    SELECT COUNT(*) INTO v_count_cursuri FROM cursuri WHERE UPPER(titlu_curs) = UPPER(p_titlu_curs);
    IF v_count_cursuri = 0 THEN
        RAISE curs_inexistent;
    END IF;
    */
    SELECT id INTO v_id_curs FROM cursuri WHERE UPPER(titlu_curs) = UPPER(p_titlu_curs);
    SELECT MAX(id) + 1 INTO v_id_nota FROM note;
    INSERT INTO note VALUES (v_id_curs, p_id_student, v_id_curs, p_valoare, SYSDATE, SYSDATE, SYSDATE);
EXCEPTION
    WHEN student_inexistent THEN
        RAISE_APPLICATION_ERROR(-20001, 'Studentul cu ID-ul ' || p_id_student || ' nu exista in baza de date.');
    WHEN curs_inexistent THEN
        RAISE_APPLICATION_ERROR(-20002, 'Cursul ' || p_titlu_curs || ' nu exista in baza de date.');
    WHEN DUP_VAL_ON_INDEX THEN
    /*
        DBMS_OUTPUT.PUT_LINE('Studentul cu ID-ul ' || p_id_student ||
                             ' are deja nota la materia ' || p_titlu_curs || '.');
    */
        NULL;
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Eroare neasteptata.');
END adauga_nota_exceptie;
/
DECLARE
    v_start_time    NUMBER;
    v_end_time      NUMBER;
BEGIN
    v_start_time := DBMS_UTILITY.GET_TIME();
    FOR i IN 1 .. 1000000 LOOP
        adauga_nota_exista(1, 'logica', 10);
    END LOOP;
    v_end_time := DBMS_UTILITY.GET_TIME();
    DBMS_OUTPUT.PUT_LINE('Metoda 1: ' || (v_end_time - v_start_time) / 100 || ' secunde'); -- 59.88 secunde
    v_start_time := DBMS_UTILITY.GET_TIME();
    FOR i IN 1 .. 1000000 LOOP
        adauga_nota_exceptie(1, 'logica', 10);
    END LOOP;
    v_end_time := DBMS_UTILITY.GET_TIME();
    DBMS_OUTPUT.PUT_LINE('Metoda 2: ' || (v_end_time - v_start_time) / 100 || ' secunde'); -- 203.69 secunde
END;
