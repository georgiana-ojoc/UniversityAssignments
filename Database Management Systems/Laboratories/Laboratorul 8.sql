SET SERVEROUTPUT ON;

/* 1 */
CREATE OR REPLACE TRIGGER dml_studenti
    BEFORE INSERT OR UPDATE OR DELETE ON studenti
BEGIN
    DBMS_OUTPUT.PUT('Operatie DML in tabela "studenti": ');
    CASE
        WHEN INSERTING THEN DBMS_OUTPUT.PUT_LINE('INSERT');
        WHEN DELETING THEN DBMS_OUTPUT.PUT_LINE('DELETE');
        WHEN UPDATING THEN DBMS_OUTPUT.PUT_LINE('UPDATE');
    END CASE;
END;
/
DELETE FROM studenti WHERE id = 10000;
/
BEGIN
    DROP_IF_EXISTS('dml_studenti', 'trigger');
END;

/* 2 */
CREATE OR REPLACE TRIGGER dml_before_studenti
    BEFORE INSERT OR UPDATE OR DELETE ON studenti
DECLARE
    v_nume studenti.nume%TYPE;
BEGIN
    SELECT nume INTO v_nume FROM studenti WHERE id = 200;
    DBMS_OUTPUT.PUT_LINE('Before DML TRIGGER: ' || v_nume);
END;
/
CREATE OR REPLACE TRIGGER dml_after_studenti
    AFTER INSERT OR UPDATE OR DELETE ON studenti
DECLARE
    v_nume studenti.nume%TYPE;
BEGIN
    SELECT nume INTO v_nume FROM studenti WHERE id = 200;
    DBMS_OUTPUT.PUT_LINE('After DML TRIGGER: ' || v_nume);
END;
/
UPDATE studenti SET nume = 'NumeNou' WHERE id = 200;
/
ROLLBACK;
/
BEGIN
    DROP_IF_EXISTS('dml_before_studenti', 'trigger');
    DROP_IF_EXISTS('dml_after_studenti', 'trigger');
END;

/* 3 */
CREATE OR REPLACE TRIGGER marire_nota
    BEFORE UPDATE OF valoare ON note
    FOR EACH ROW
BEGIN
    DBMS_OUTPUT.PUT_LINE('ID nota: ' || :OLD.id);
    DBMS_OUTPUT.PUT_LINE('Vechea nota: ' || :OLD.valoare);
    DBMS_OUTPUT.PUT_LINE('Noua nota: ' || :NEW.valoare);
    IF (:OLD.valoare > :NEW.valoare) THEN :NEW.valoare := :OLD.valoare;
    END IF;
END;
/
UPDATE note SET valoare = 8 WHERE id IN (1, 2, 3, 4);
/
ROLLBACK;
/
BEGIN
    DROP_IF_EXISTS('marire_nota', 'trigger');
END;

/* 4 */
CREATE OR REPLACE TRIGGER mutate_example
    AFTER DELETE ON note
    FOR EACH ROW
DECLARE
    v_ramase INT;
BEGIN
    DBMS_OUTPUT.PUT_LINE('Stergere nota cu ID: ' || :OLD.id);
    SELECT COUNT(*) INTO v_ramase FROM note;
    DBMS_OUTPUT.PUT_LINE('Au ramas ' || v_ramase || ' note.');
END;
/
DELETE FROM note WHERE id BETWEEN 101 AND 110;
/
ROLLBACK;
/
BEGIN
    DROP_IF_EXISTS('mutate_example', 'trigger');
END;

/* 5 */
CREATE OR REPLACE TRIGGER stergere_note
    FOR DELETE ON NOTE
COMPOUND TRIGGER
    v_ramase INT;
AFTER EACH ROW IS
BEGIN
    DBMS_OUTPUT.PUT_LINE('Stergere nota cu ID: ' || :OLD.id);
END AFTER EACH ROW;
AFTER STATEMENT IS BEGIN
    SELECT COUNT(*) INTO v_ramase FROM note;
    DBMS_OUTPUT.PUT_LINE('Au ramas ' || v_ramase || ' note.');
END AFTER STATEMENT;
END stergere_note;
/
DELETE FROM note WHERE id BETWEEN 241 AND 250;
/
ROLLBACK;
/
BEGIN
    DROP_IF_EXISTS('stergere_note', 'trigger');
END;

/* 6 */
CREATE VIEW view_studenti AS SELECT * FROM studenti;
/
CREATE OR REPLACE TRIGGER delete_student
    INSTEAD OF DELETE ON view_studenti
BEGIN
    DBMS_OUTPUT.PUT_LINE('Stergem pe: ' || :OLD.nume);
    DELETE FROM note WHERE id_student = :OLD.id;
    DELETE FROM prieteni WHERE id_student1 = :OLD.id;
    DELETE FROM prieteni WHERE id_student2 = :OLD.id;
    DELETE FROM studenti WHERE id = :OLD.id;
END;
/
DELETE FROM view_studenti WHERE id = 75;
/
BEGIN
    DROP_IF_EXISTS('delete_student', 'trigger');
END;
/
ROLLBACK;

/* 7 */
CREATE OR REPLACE TRIGGER drop_trigger
    BEFORE DROP ON student.SCHEMA
BEGIN
    RAISE_APPLICATION_ERROR (num => -20000, msg => 'can''t touch this');
END;
/
DROP TABLE NOTE;
/
BEGIN
    DROP_IF_EXISTS('drop_trigger', 'trigger');
END;
/
ROLLBACK;

/* 8 */
CREATE OR REPLACE TRIGGER create_trigger
    INSTEAD OF CREATE ON SCHEMA
BEGIN
    EXECUTE IMMEDIATE 'CREATE TABLE t (n NUMBER, m NUMBER)';
END;
/
CREATE TABLE a (x NUMBER);
/
BEGIN
    DROP_IF_EXISTS('create_trigger', 'trigger');
END;
/
ROLLBACK;

/* 9 */
CREATE TABLE autentificari (nume VARCHAR2(30), ora TIMESTAMP);
/

/
CREATE OR REPLACE TRIGGER check_user
    AFTER LOGON ON DATABASE
DECLARE
    v_nume VARCHAR2(30);
BEGIN
    v_nume := ora_login_user;
    INSERT INTO autentificari VALUES(v_nume, CURRENT_TIMESTAMP);
END;
/
SELECT * FROM autentificari;
/
BEGIN
    DROP_IF_EXISTS('check_user', 'trigger');
END;
/
ROLLBACK;
