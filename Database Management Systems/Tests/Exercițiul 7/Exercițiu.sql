SET SERVEROUTPUT ON;

CREATE OR REPLACE VIEW view_catalog AS
    SELECT nume, prenume, titlu_curs, valoare
    FROM studenti s JOIN note n ON s.id = n.id_student
    JOIN cursuri c ON n.id_curs = c.id;
/
CREATE OR REPLACE FUNCTION insereaza_student(p_nume studenti.nume%TYPE, p_prenume studenti.prenume%TYPE)
    RETURN studenti.id%TYPE AS
    v_id                studenti.id%TYPE;
    v_nr_matricol       studenti.nr_matricol%TYPE;
    v_an                studenti.an%TYPE;
    v_grupa             studenti.grupa%TYPE;
    v_bursa             studenti.bursa%TYPE;
    v_data_nastere      studenti.data_nastere%TYPE;
    v_email             studenti.email%TYPE;
    v_random_character  CHARACTER;
    v_random_digit      NUMBER(1);
BEGIN
    SELECT MAX(id) + 1 INTO v_id FROM studenti;
    v_nr_matricol := DBMS_RANDOM.STRING('X', 6);
    v_an := TRUNC(DBMS_RANDOM.VALUE(1, 4));
    v_random_character := CHR(DBMS_RANDOM.VALUE(ASCII('A'), ASCII('C')));
    v_random_digit := DBMS_RANDOM.VALUE(1, 7);
    v_grupa := v_random_character || v_random_digit;
    v_bursa := TRUNC(DBMS_RANDOM.VALUE(5, 16)) * 100;
    v_data_nastere := SYSDATE - DBMS_RANDOM.VALUE(365 * 20, 365 * 25);
    v_email := LOWER(p_nume) || '.' || LOWER(p_prenume) || '@gmail.com';
    INSERT INTO studenti VALUES (v_id, v_nr_matricol, p_nume, p_prenume, v_an, v_grupa, v_bursa, v_data_nastere,
                                 v_email, SYSDATE, SYSDATE);
    RETURN v_id;
END;
/
CREATE OR REPLACE FUNCTION insereaza_curs(p_titlu_curs cursuri.titlu_curs%TYPE) RETURN cursuri.id%TYPE AS
    v_id            cursuri.id%TYPE;
    v_an            cursuri.an%TYPE;
    v_semestru      cursuri.semestru%TYPE;
    v_credite       cursuri.credite%TYPE;
BEGIN
    SELECT MAX(id) + 1 INTO v_id FROM cursuri;
    v_an := TRUNC(DBMS_RANDOM.VALUE(1, 4));
    v_semestru := TRUNC(DBMS_RANDOM.VALUE(1, 3));
    v_credite := TRUNC(DBMS_RANDOM.VALUE(4, 7));
    INSERT INTO cursuri VALUES (v_id, p_titlu_curs, v_an, v_semestru, v_credite, SYSDATE, SYSDATE);
    RETURN v_id;
END;
/
CREATE OR REPLACE PROCEDURE insereaza_nota(p_id_student note.id_student%TYPE, p_id_curs note.id_curs%TYPE,
    p_valoare note.valoare%TYPE) AS
    v_id            note.id%TYPE;
    v_data_notare   note.data_notare%TYPE;
BEGIN
    SELECT MAX(id) + 1 INTO v_id FROM note;
    v_data_notare := SYSDATE - DBMS_RANDOM.VALUE(0, 365 * 5);
    INSERT INTO note VALUES (v_id, p_id_student, p_id_curs, p_valoare, v_data_notare, SYSDATE, SYSDATE);
END;
/
CREATE OR REPLACE TRIGGER insert_nota_catalog
    INSTEAD OF INSERT ON view_catalog
DECLARE
    v_studenti      NUMBER;
    v_cursuri       NUMBER;
    v_note          NUMBER;
    v_id_student    studenti.id%TYPE;
    v_id_curs       cursuri.id%TYPE;
BEGIN
    SELECT COUNT(*) INTO v_studenti FROM studenti WHERE id =
    (SELECT id FROM studenti WHERE LOWER(nume) = LOWER(:NEW.nume) AND LOWER(prenume) = LOWER(:NEW.prenume));
    IF v_studenti = 0
        THEN
            v_id_student := insereaza_student(:NEW.nume, :NEW.prenume);
            DBMS_OUTPUT.PUT_LINE('S-a inserat studentul ' || :NEW.nume || ' ' || :NEW.prenume || '.');
        ELSE
            SELECT id INTO v_id_student FROM studenti WHERE LOWER(nume) = LOWER(:NEW.nume)
                                                    AND LOWER(prenume) = LOWER(:NEW.prenume);
    END IF;
    SELECT COUNT(*) INTO v_cursuri FROM cursuri WHERE LOWER(titlu_curs) = LOWER(:NEW.titlu_curs);
    IF v_cursuri = 0
        THEN
            v_id_curs := insereaza_curs(:NEW.titlu_curs);
            DBMS_OUTPUT.PUT_LINE('S-a inserat cursul ' || :NEW.titlu_curs || '.');
        ELSE
            SELECT id INTO v_id_curs FROM cursuri WHERE LOWER(titlu_curs) = LOWER(:NEW.titlu_curs);
    END IF;
    SELECT COUNT(*) INTO v_note FROM note WHERE id_student = v_id_student AND id_curs = v_id_curs;
    IF v_note = 0
        THEN
            insereaza_nota(v_id_student, v_id_curs, :NEW.valoare);
        ELSE
            DBMS_OUTPUT.PUT_LINE(:NEW.nume || ' ' || :NEW.prenume || ' are deja nota la '
                || :NEW.titlu_curs || '.');
    END IF;
END;
/
CREATE OR REPLACE TRIGGER update_nota_catalog
    INSTEAD OF UPDATE ON view_catalog
DECLARE
    v_studenti      NUMBER;
    v_cursuri       NUMBER;
    v_note          NUMBER;
    v_id_student    studenti.id%TYPE;
    v_id_curs       cursuri.id%TYPE;
BEGIN
    SELECT COUNT(*) INTO v_studenti FROM studenti WHERE id =
    (SELECT id FROM studenti WHERE LOWER(nume) = LOWER(:NEW.nume) AND LOWER(prenume) = LOWER(:NEW.prenume));
    IF v_studenti = 0
        THEN
            v_id_student := insereaza_student(:NEW.nume, :NEW.prenume);
            DBMS_OUTPUT.PUT_LINE('S-a inserat studentul ' || :NEW.nume || ' ' || :NEW.prenume || '.');
        ELSE
            SELECT id INTO v_id_student FROM studenti WHERE LOWER(nume) = LOWER(:NEW.nume)
                                                    AND LOWER(prenume) = LOWER(:NEW.prenume);
    END IF;
    SELECT COUNT(*) INTO v_cursuri FROM cursuri WHERE LOWER(titlu_curs) = LOWER(:NEW.titlu_curs);
    IF v_cursuri = 0
        THEN
            v_id_curs := insereaza_curs(:NEW.titlu_curs);
            DBMS_OUTPUT.PUT_LINE('S-a inserat cursul ' || :NEW.titlu_curs || '.');
        ELSE
            SELECT id INTO v_id_curs FROM cursuri WHERE LOWER(titlu_curs) = LOWER(:NEW.titlu_curs);
    END IF;
    SELECT COUNT(*) INTO v_note FROM note WHERE id_student = v_id_student AND id_curs = v_id_curs;
    IF v_note = 0
        THEN
            insereaza_nota(v_id_student, v_id_curs, :NEW.valoare);
            DBMS_OUTPUT.PUT_LINE('S-a inserat nota ' || :NEW.valoare || '.');
        ELSIF (:OLD.valoare > :NEW.valoare)
            THEN
                DBMS_OUTPUT.PUT_LINE('Nota veche este mai mare ca nota noua.');
            ELSIF (:OLD.valoare = :NEW.valoare)
                THEN
                    DBMS_OUTPUT.PUT_LINE('Nota veche este egala ca nota noua.');
                ELSE
                    UPDATE note SET valoare = :NEW.valoare, updated_at = SYSDATE
                    WHERE id_student = v_id_student AND id_curs = v_id_curs;
    END IF;
END;
/
CREATE OR REPLACE TRIGGER delete_student_catalog
    INSTEAD OF DELETE ON view_catalog
DECLARE
    v_studenti  NUMBER;
    v_id        studenti.id%TYPE;
BEGIN
    SELECT COUNT(*) INTO v_studenti FROM studenti WHERE id =
    (SELECT id FROM studenti WHERE LOWER(nume) = LOWER(:OLD.nume) AND LOWER(prenume) = LOWER(:OLD.prenume));
    IF v_studenti = 0
        THEN
            DBMS_OUTPUT.PUT_LINE('Studentul ' || :OLD.nume || ' ' || :OLD.prenume || ' nu exista.');
        ELSE
            SELECT id INTO v_id FROM studenti WHERE LOWER(nume) = LOWER(:OLD.nume)
                                                AND LOWER(prenume) = LOWER(:OLD.prenume);
            DELETE FROM note WHERE id_student = v_id;
            DELETE FROM prieteni WHERE id_student1 = v_id;
            DELETE FROM prieteni WHERE id_student2 = v_id;
            DELETE FROM studenti WHERE id = v_id;
    END IF;
END;
/
INSERT INTO view_catalog VALUES ('Potter', 'Harry', 'History of Magic', 10);
/
UPDATE view_catalog SET valoare = 10 WHERE nume = 'Potter' AND prenume = 'Harry' AND titlu_curs = 'History of Magic';
/
DELETE FROM view_catalog WHERE nume = 'Potter' AND prenume = 'Harry';
/
DROP TRIGGER insert_nota_catalog;
/
DROP TRIGGER update_nota_catalog;
/
DROP TRIGGER delete_student_catalog;
/
ROLLBACK;
