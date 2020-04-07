SET SERVEROUTPUT ON;
DECLARE
    v_profesori_nume profesori.nume%TYPE DEFAULT '';
    v_profesori_prenume profesori.prenume%TYPE DEFAULT '';
    v_count_note NUMBER DEFAULT 0;
BEGIN
    SELECT nume, prenume INTO v_profesori_nume, v_profesori_prenume
    FROM profesori p JOIN didactic d ON p.id = d.id_profesor GROUP BY id_profesor, nume, prenume
    HAVING COUNT(id_curs) = (SELECT MAX(COUNT(id_curs)) FROM didactic GROUP BY id_profesor);
    DBMS_OUTPUT.PUT_LINE('Nume si prenume profesor: ' || v_profesori_nume || ' ' || v_profesori_prenume);
    DBMS_OUTPUT.PUT_LINE('Lungime nume si prenume profesor: ' || LENGTH(v_profesori_nume || ' ' || v_profesori_prenume));
    SELECT COUNT(valoare) INTO v_count_note
    FROM profesori p JOIN didactic d ON p.id = d.id_profesor JOIN note n ON d.id_curs = n.id_curs
    WHERE nume = v_profesori_nume AND prenume = v_profesori_prenume AND valoare = 1;
    IF (v_count_note > 0)
        THEN
            DBMS_OUTPUT.PUT_LINE(v_profesori_nume || ' ' || v_profesori_prenume || ' a pus ' || v_count_note
            || ' note de 10.');
        ELSE
            DBMS_OUTPUT.PUT_LINE(v_profesori_nume || ' ' || v_profesori_prenume || ' nu a pus nicio nota de 10.');
    END IF;
END;