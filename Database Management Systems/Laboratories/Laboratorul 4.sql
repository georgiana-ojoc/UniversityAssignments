/* 1 */
SET SERVEROUTPUT ON;

CREATE OR REPLACE PROCEDURE afiseaza AS
    my_name VARCHAR2(20) := 'Gigel';
BEGIN
   DBMS_OUTPUT.PUT_LINE('Ma cheama ' || my_name);
END afiseaza;

BEGIN
    afiseaza();
END;

SELECT * FROM user_objects;
SELECT * FROM user_procedures;
SELECT text FROM user_source WHERE LOWER(name) LIKE 'afiseaza';

DROP PROCEDURE afiseaza;



/* 2 */
SET SERVEROUTPUT ON;

CREATE OR REPLACE PROCEDURE inc_numar(p_val IN OUT NUMBER) AS
BEGIN
   p_val := p_val + 1;
END;

DECLARE
   v_numar VARCHAR2(10) := '7';
BEGIN
   inc_numar(v_numar);
   DBMS_OUTPUT.PUT_LINE(v_numar);
END;



/* 3 */
SET SERVEROUTPUT ON;

CREATE OR REPLACE PROCEDURE inc_data(p_data IN OUT DATE) AS
BEGIN
   p_data := p_data + 1;
END;

DECLARE
   v_data DATE DEFAULT SYSDATE;
BEGIN
   inc_data(v_data);
   DBMS_OUTPUT.PUT_LINE(v_data);
END;



/* 4 */
SET SERVEROUTPUT ON;

CREATE OR REPLACE PROCEDURE sum(p_val1 IN NUMBER, p_val2 IN NUMBER, p_suma OUT NUMBER) AS
BEGIN
   p_suma := p_val1 + p_val2;
END;

DECLARE
   v_suma NUMBER DEFAULT 0;
BEGIN
   suma(1, 2, v_suma);
   DBMS_OUTPUT.PUT_LINE(v_suma);
END;



/* 5 */
SET SERVEROUTPUT ON;

CREATE OR REPLACE PROCEDURE pow1(p_baza IN INTEGER := 3, p_exponent IN INTEGER DEFAULT 5) AS
   v_rezultat INTEGER;
BEGIN
    v_rezultat := p_baza ** p_exponent;
    DBMS_OUTPUT.PUT_LINE(v_rezultat);
END;

BEGIN
   pow1(2, 3);
END;

BEGIN
   pow1(p_baza => 2, p_exponent => 3);
END;

BEGIN
   pow1(p_exponent => 3, p_baza => 2);
END;

BEGIN
   pow1(2, p_exponent => 3);
END;

BEGIN
   pow1(p_baza => 2);
END;

BEGIN
   pow1(p_exponent => 3);
END;



/* 6 */
SET SERVEROUTPUT ON;

CREATE OR REPLACE PROCEDURE pow2(p_baza IN INTEGER := 3, p_exponent IN INTEGER DEFAULT 5, p_out OUT Integer) AS   
BEGIN
    p_out := p_baza ** p_exponent;    
END;

DECLARE 
   v_out INTEGER;
BEGIN
   pow2(p_baza => 3, p_out => v_out);
   DBMS_OUTPUT.PUT_LINE(v_out);
END;



/* 7 */
SET SERVEROUTPUT ON;

CREATE OR REPLACE FUNCTION make_waves(v_sir VARCHAR2) RETURN VARCHAR2 AS
   v_rezultat VARCHAR2(1000) := '';
BEGIN
    FOR v_index IN 1..length(v_sir) LOOP
        IF (v_index MOD 2 = 1)
            THEN
                v_rezultat := v_rezultat || UPPER(SUBSTR(v_sir, v_index, 1));
            ELSE
                v_rezultat := v_rezultat || LOWER(SUBSTR(v_sir, v_index, 1));
        END IF;
    END LOOP;
    RETURN v_rezultat;
END;

SELECT make_waves('Facultatea de informatica') FROM dual;

DECLARE
   v_sir VARCHAR2(1000) := 'Facultatea de informatica';
BEGIN
   v_sir := make_waves(v_sir);
   DBMS_OUTPUT.PUT_LINE(v_sir);
END;



/* 8 */
SET SERVEROUTPUT ON;

CREATE OR REPLACE PACKAGE manager_facultate AS
    data_azi DATE:= SYSDATE;
    PROCEDURE adauga_student(nume studenti.nume%TYPE, prenume studenti.prenume%TYPE);
    PROCEDURE sterge_student(nr_matricol studenti.nr_matricol%TYPE);
END manager_facultate;

CREATE OR REPLACE PACKAGE BODY manager_facultate AS
    nume_facultate VARCHAR2(100) := 'Facultatea de Informatica din Iasi';
    
    FUNCTION calculeaza_varsta(data_nastere DATE) RETURN INT AS
    BEGIN
        RETURN FLOOR((data_azi - data_nastere) / 365);
    END calculeaza_varsta;   

    PROCEDURE adauga_student(nume studenti.nume%TYPE, prenume studenti.prenume%TYPE) AS
    BEGIN
        DBMS_OUTPUT.PUT_LINE('Varsta: ' || calculeaza_varsta(to_date('01/01/1990', 'DD/MM/YYYY')));
        INSERT INTO studenti (id, nr_matricol, nume, prenume) SELECT (SELECT MAX(id) + 1 FROM studenti),
        '1Z1Z1Z', nume, prenume FROM dual;
    END adauga_student;
    
    PROCEDURE sterge_student(nr_matricol studenti.nr_matricol%TYPE) AS
    BEGIN
        null;
    END sterge_student;
END manager_facultate;

BEGIN
   manager_facultate.adauga_student('Becali', 'Gigi');
END;



/* 9 */
SET SERVEROUTPUT ON;

CREATE OR REPLACE FUNCTION exista_student(p_id IN studenti.ID%TYPE) RETURN BOOLEAN AS
    v_exista NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_exista FROM studenti WHERE id = p_id;
    IF v_exista = 0
        THEN
            DBMS_OUTPUT.PUT_LINE('Studentul cu id-ul ' || p_id || ' nu exista in baza de date.');
            RETURN FALSE;
        ELSE
            RETURN TRUE;
    END IF;
END exista_student;



/* 10 */
SET SERVEROUTPUT ON;

CREATE OR REPLACE PROCEDURE get_medii_student(p_id IN studenti.ID%TYPE, p_medie1 OUT FLOAT, p_medie2 OUT FLOAT) AS
    v_an NUMBER(1);
    v_exista BOOLEAN;
BEGIN
    v_exista := exista_student(p_id);
    IF v_exista = TRUE
        THEN
            SELECT an INTO v_an FROM studenti WHERE id = p_id;
            IF v_an = 1
                THEN 
                    DBMS_OUTPUT.PUT_LINE('Studentul cu id-ul ' || p_id || ' este in anul 1.');
            ELSIF v_an = 2
                THEN
                    DBMS_OUTPUT.PUT_LINE('Studentul cu id-ul ' || p_id || ' este in anul 2.');
                    SELECT TRUNC(AVG(valoare), 2) INTO p_medie1 FROM note n JOIN cursuri c ON n.id_curs = c.id
                    WHERE an = 1 AND id_student = p_id;
            ELSIF v_an = 3
                THEN
                    DBMS_OUTPUT.PUT_LINE('Studentul cu id-ul ' || p_id || ' este in anul 3.');
                    SELECT TRUNC(AVG(valoare), 2) INTO p_medie1 FROM note n JOIN cursuri c ON n.id_curs = c.id
                    WHERE an = 1 AND id_student = p_id;
                    SELECT TRUNC(AVG(valoare), 2) INTO p_medie2 FROM note n JOIN cursuri c ON n.id_curs = c.id
                    WHERE an = 2 AND id_student = p_id;
            END IF;
    END IF;
END;

DECLARE 
    v_id studenti.ID%TYPE := '500';
    v_medie1 NUMBER(4,2);
    v_medie2 NUMBER(4,2);
BEGIN
  get_medii_student(v_id, v_medie1, v_medie2);
  DBMS_OUTPUT.PUT_LINE('Media din anul I este: ' || v_medie1 || '.');
  DBMS_OUTPUT.PUT_LINE('Media din anul II este: ' || v_medie2 || '.');
END;



/* 11 */
SET SERVEROUTPUT ON;

CREATE OR REPLACE PROCEDURE afiseaza_varsta AS
    v_numar_studenti NUMBER;
    v_student_random NUMBER(5);
    v_rezultat VARCHAR(1000);
BEGIN
    SELECT COUNT(*) INTO v_numar_studenti FROM studenti;
    v_student_random := DBMS_RANDOM.VALUE(1, v_numar_studenti + 1);
    SELECT id || ' ' || nume || ' ' || prenume || ' ' || varsta INTO v_rezultat FROM
    (SELECT id, nume, prenume, TRUNC(MONTHS_BETWEEN(SYSDATE, data_nastere) / 12) || ' de ani, ' ||
    MOD(FLOOR(MONTHS_BETWEEN(SYSDATE, data_nastere)), 12) || ' luni si ' ||
    FLOOR(SYSDATE - ADD_MONTHS(data_nastere, TRUNC(MONTHS_BETWEEN(SYSDATE, data_nastere)))) || ' zile'
    AS varsta, ROWNUM AS rand FROM studenti) WHERE rand = v_student_random;
    DBMS_OUTPUT.PUT_LINE(v_rezultat);
END; 

BEGIN
afiseaza_varsta;
END;



/* 12 */
SET SERVEROUTPUT ON;

CREATE OR REPLACE TYPE student_object AS OBJECT(
    student_id NUMBER,
    student_nume VARCHAR2(100),
    student_prenume VARCHAR2(100)
);

CREATE OR REPLACE TYPE student_table AS TABLE OF student_object;

CREATE OR REPLACE FUNCTION get_students_details RETURN student_table AS
    student_table_array student_table := student_table();
BEGIN
  SELECT student_object(id, nume, prenume) BULK COLLECT INTO student_table_array FROM
  (SELECT s.id AS id, s.nume AS nume, s.prenume AS prenume
  FROM studenti s JOIN note n ON s.id = n.id_student JOIN cursuri c ON n.id_curs = c.id
  WHERE valoare = 10 AND titlu_curs = 'Baze de date'); 
  RETURN student_table_array;
END;

SELECT * FROM TABLE (get_students_details) ORDER BY 1;
