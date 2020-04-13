SET SERVEROUTPUT ON;

-- CONNECT SYSTEM AS SYSDBA;
-- ALTER SESSION SET "_ORACLE_SCRIPT" = true;
-- DROP USER STUDENT2;
-- CREATE USER STUDENT2 IDENTIFIED BY STUDENT DEFAULT TABLESPACE USERS TEMPORARY TABLESPACE TEMP;
-- GRANT ALL PRIVILEGES TO STUDENT2;
CREATE OR REPLACE TRIGGER database_logon
    AFTER LOGON ON DATABASE
BEGIN
   IF (UPPER(USER) = 'STUDENT2') THEN
        EXECUTE IMMEDIATE 'ALTER SESSION SET current_schema = STUDENT';
        DBMS_APPLICATION_INFO.SET_MODULE(USER, 'Current_Schema Set');
   END IF;
END;
/
DROP TABLE log_note;
/
CREATE TABLE log_note
    (id_nota NUMBER,
    nota_veche NUMBER(2),
    nota_noua NUMBER(2),
    tip_operatie VARCHAR2(10),
    data_operatie TIMESTAMP,
    username VARCHAR2(25));
/
CREATE OR REPLACE TRIGGER istoric_note
    AFTER INSERT OR UPDATE OR DELETE ON note
    FOR EACH ROW
DECLARE
    v_user log_note.username%TYPE;
BEGIN
    SELECT user INTO v_user FROM DUAL;
    CASE
        WHEN INSERTING THEN INSERT INTO log_note VALUES(:OLD.id, NULL, :NEW.valoare, 'INSERT', SYSDATE, v_user);
        WHEN UPDATING THEN INSERT INTO log_note VALUES(:OLD.id, :OLD.valoare, :NEW.valoare, 'UPDATE', SYSDATE, v_user);
        WHEN DELETING THEN INSERT INTO log_note VALUES(:OLD.id, :OLD.valoare, NULL, 'DELETE', SYSDATE, v_user);
    END CASE;
END;
/
DECLARE
    v_id_nota       note.id%TYPE;
    v_id_student    note.id_student%TYPE;
    V_id_curs       note.id_curs%TYPE;
BEGIN
    SELECT id, id_student, id_curs INTO v_id_nota, v_id_student, v_id_curs FROM note
    WHERE id = (SELECT MAX(id) FROM note);
    UPDATE note SET valoare = 10 WHERE id = v_id_nota;
    DELETE FROM note WHERE id = v_id_nota;
    INSERT INTO note VALUES(v_id_nota, v_id_student, v_id_curs, 9, SYSDATE, SYSDATE, SYSDATE);
END;
/
SELECT * FROM log_note;
/
DROP TRIGGER istoric_note;
/
ROLLBACK;
