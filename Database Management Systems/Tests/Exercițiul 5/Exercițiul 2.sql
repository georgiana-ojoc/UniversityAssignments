SET SERVEROUTPUT ON;

CREATE OR REPLACE FUNCTION calculeaza_medie(p_nume studenti.nume%TYPE, p_prenume studenti.prenume%TYPE)
    RETURN NUMBER AS
    student_inexistent EXCEPTION;
    PRAGMA EXCEPTION_INIT(student_inexistent, -20001);
    note_inexistente EXCEPTION;
    PRAGMA EXCEPTION_INIT(note_inexistente, -20002);
    v_count_studenti    NUMBER;
    v_id                studenti.id%TYPE;
    v_count_note        NUMBER;
    v_medie             NUMBER(4, 2);
BEGIN
    SELECT COUNT(*) INTO v_count_studenti FROM studenti
    WHERE UPPER(nume) = UPPER(p_nume) AND UPPER(prenume) = UPPER(p_prenume);
    IF v_count_studenti = 0 THEN
        RAISE student_inexistent;
    END IF;
    SELECT id INTO v_id FROM studenti
    WHERE UPPER(nume) = UPPER(p_nume) AND UPPER(prenume) = UPPER(p_prenume);
    SELECT COUNT(*) INTO v_count_note FROM note WHERE id_student = id;
    IF v_count_note = 0 THEN
        RAISE note_inexistente;
    END IF;
    SELECT AVG(valoare) INTO v_medie FROM note WHERE id_student = v_id;
    RETURN v_medie;
END;
/
DECLARE
    student_inexistent EXCEPTION;
    PRAGMA EXCEPTION_INIT(student_inexistent, -20001);
    note_inexistente EXCEPTION;
    PRAGMA EXCEPTION_INIT(note_inexistente, -20002);
    TYPE t_nume_intreg IS RECORD(nume studenti.nume%TYPE, prenume studenti.prenume%TYPE);
    TYPE t_nume_intregi IS VARYING ARRAY(6) OF t_nume_intreg;
    v_nume_intregi  t_nume_intregi;
    v_studenti      NUMBER;
    v_medie         NUMBER(4, 2);
BEGIN
    BEGIN
    v_nume_intregi := t_nume_intregi();
    v_nume_intregi.EXTEND(6);
    SELECT COUNT(*) INTO v_studenti FROM studenti;
    FOR i IN 1 .. 3 LOOP
        SELECT nume, prenume INTO v_nume_intregi(i).nume, v_nume_intregi(i).prenume FROM studenti
        WHERE id = TRUNC(DBMS_RANDOM.VALUE(1, v_studenti + 1));
    END LOOP;
    v_nume_intregi(4).nume := 'Shelby';
    v_nume_intregi(4).prenume := 'Tommy';
    v_nume_intregi(5).nume := 'Shelby';
    v_nume_intregi(5).prenume := 'Arthur';
    v_nume_intregi(6).nume := 'Shelby';
    v_nume_intregi(6).prenume := 'Ada';
    FOR i IN 1 .. 6 LOOP
        v_medie := calculeaza_medie(v_nume_intregi(i).nume, v_nume_intregi(i).prenume);
        DBMS_OUTPUT.PUT_LINE('Studentul ' || v_nume_intregi(i).nume || ' ' || v_nume_intregi(i).prenume ||
                             ' are media ' || v_medie || '.');
    END LOOP;
EXCEPTION
    WHEN student_inexistent THEN
        RAISE_APPLICATION_ERROR(-20001, 'Studentul nu exista in baza de date.');
    WHEN note_inexistente THEN
        RAISE_APPLICATION_ERROR(-20002, 'Studentul nu are nicio nota in baza de date.');
    END;
END;
