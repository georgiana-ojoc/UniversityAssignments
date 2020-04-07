/* 1 */
SET SERVEROUTPUT ON;
ACCEPT i_numar PROMPT "Please enter your number: "; 
DECLARE
    v_numar NUMBER(5);
    i_numar NUMBER(5);
BEGIN   
    v_numar := &i_numar;
    IF (v_numar MOD 2 = 0)
        THEN 
            DBMS_OUTPUT.PUT_LINE('Numarul '|| v_numar || ' este par.');
        ELSE
            DBMS_OUTPUT.PUT_LINE('Numarul ' || v_numar || ' este impar.');
    END IF;
END; 



/* 2 */
SET SERVEROUTPUT ON;
DECLARE
    v_numar NUMBER(5) := 50;
BEGIN
    IF (v_numar < 10)
        THEN
            DBMS_OUTPUT.PUT_LINE('Numarul este mai mic decat 10.');
        ELSIF (v_numar > 80)
            THEN
                DBMS_OUTPUT.PUT_LINE('Numarul este mai mare decat 80.');
            ELSE
                DBMS_OUTPUT.PUT_LINE('Numarul este in intervalul [10, 80].');  
    END IF;
END;



/* 3 */
SET SERVEROUTPUT ON;
DECLARE
    numar NUMBER := 5;
BEGIN
    CASE (numar)
        WHEN 1 THEN DBMS_OUTPUT.PUT_LINE('Primul numar natural nenul');
        WHEN 2 THEN DBMS_OUTPUT.PUT_LINE('Primul numar natural par');
        ELSE DBMS_OUTPUT.PUT_LINE('Un numar mai mare sau egal cu 3');
    END CASE;
END;



/* 4 */
SELECT nume, prenume,
CASE bursa
    WHEN 700 THEN 'Sapte sute'
    WHEN 800 THEN 'Opt sute'
    WHEN 900 THEN 'Noua sute'
    ELSE 'Fara bursa sau alta suma'
END
AS bursa
FROM studenti;

SELECT nume, prenume,
CASE 
    WHEN bursa > 1000 THEN 'Bogat'      
    ELSE 'Sarac'
END
FROM studenti;



/* 5 */
SET SERVEROUTPUT ON;
DECLARE
    v_contor INTEGER := 0;
BEGIN
    WHILE (v_contor < 10) LOOP       
        v_contor := v_contor + 1;
        DBMS_OUTPUT.PUT_LINE(v_contor);  
    END LOOP;
END;



/* 6 */
SET SERVEROUTPUT ON;
DECLARE
    v_contor INTEGER := 0;
BEGIN
    LOOP
        v_contor := v_contor + 1;
        DBMS_OUTPUT.PUT_LINE(v_contor);  
        EXIT WHEN v_contor = 10;
   END LOOP;
END;



/* 7 */
SET SERVEROUTPUT ON;
DECLARE
    v_contor INTEGER := 0;
BEGIN
    FOR v_contor IN REVERSE 1..10 LOOP
        DBMS_OUTPUT.PUT_LINE(v_contor);  
    END LOOP;
END;



/* 8 */
SET SERVEROUTPUT ON;
DECLARE
    v_contor1 INTEGER;
    v_contor2 INTEGER;
BEGIN
    <<eticheta>>
    FOR v_contor1 IN 1..5 LOOP
        FOR v_contor2 IN 10..20 LOOP
            CONTINUE WHEN ((v_contor1 = 3) AND (v_contor2 = 13));
            DBMS_OUTPUT.PUT_LINE(v_contor1 || '-' || v_contor2);  
            EXIT eticheta WHEN ((v_contor1 = 3) AND (v_contor2 = 16));
        END LOOP;
    END LOOP;
END;



/* 9 */
SET SERVEROUTPUT ON;
DECLARE
    v_contor INTEGER DEFAULT 0;
BEGIN
    <<eticheta1>>
    GOTO eticheta2;
    DBMS_OUTPUT.PUT_LINE('Nu se va afisa.');
    <<eticheta2>>
    IF v_contor < 10
        THEN
            v_contor := v_contor + 1;
            DBMS_OUTPUT.PUT_LINE(v_contor || ': Se va afisa.');
            GOTO eticheta1;
    END IF;
END;



/* 10 */
SET SERVEROUTPUT ON;
DECLARE
BEGIN  
    UPDATE studenti SET bursa = bursa + 10 WHERE bursa > 300;
    IF (SQL%FOUND) 
        THEN
            DBMS_OUTPUT.PUT_LINE('Am marit bursa la ' || SQL%ROWCOUNT || ' studenti.');
        ELSE
            DBMS_OUTPUT.PUT_LINE('Nimanui nu i s-a marit bursa.');
    END IF;
END;



/* 11 */
SET SERVEROUTPUT ON;
DECLARE
    CURSOR lista_studenti_bursieri IS
    SELECT nume, prenume FROM studenti WHERE bursa IS NOT NULL;
    v_nume studenti.nume%TYPE;       
    v_prenume studenti.prenume%TYPE;
BEGIN
    OPEN lista_studenti_bursieri;
    LOOP
        FETCH lista_studenti_bursieri INTO v_nume, v_prenume;
        EXIT WHEN lista_studenti_bursieri%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE(lista_studenti_bursieri%ROWCOUNT || ': ' || v_nume || ' ' || v_prenume);
    END LOOP;
    CLOSE lista_studenti_bursieri;  
END;



/* 12 */
SET SERVEROUTPUT ON;
DROP TABLE indivizi;
CREATE TABLE indivizi(nume VARCHAR2(30), prenume VARCHAR2(30));
INSERT INTO indivizi SELECT UPPER(nume), prenume FROM studenti;
INSERT INTO Indivizi SELECT UPPER(nume), prenume FROM profesori;



/* 13 */
SET SERVEROUTPUT ON;
DECLARE
    CURSOR lista_studenti IS
    SELECT * FROM studenti;
    v_studenti_linie lista_studenti%ROWTYPE;       
BEGIN
    OPEN lista_studenti;
    LOOP
        FETCH lista_studenti INTO v_studenti_linie;
        EXIT WHEN lista_studenti%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE(v_studenti_linie.nume || ' ' || v_studenti_linie.data_nastere);
    END LOOP;
    CLOSE lista_studenti;  
END;



/* 14 */
SET SERVEROUTPUT ON;
DECLARE
    CURSOR lista_studenti IS
    SELECT * FROM studenti;
BEGIN
    FOR v_studenti_linie IN lista_studenti LOOP     
        DBMS_OUTPUT.PUT_LINE(v_studenti_linie.nume || ' ' || v_studenti_linie.data_nastere);
    END LOOP;  
END;



/* 15 */
SET SERVEROUTPUT ON;
BEGIN
    FOR v_std_linie IN (SELECT * FROM studenti) LOOP     
        DBMS_OUTPUT.PUT_LINE(v_std_linie.nume || ' ' || v_std_linie.data_nastere);
    END LOOP;  
END;



/* 16 */
SET SERVEROUTPUT ON;
DECLARE
    CURSOR lista_studenti_bursieri(p_bursa studenti.bursa%TYPE, p_an studenti.an%TYPE) IS
    SELECT nume, prenume FROM studenti WHERE bursa > p_bursa AND an > p_an;
    v_studenti_linie lista_studenti_bursieri%ROWTYPE;       
BEGIN
    /*
    OPEN lista_studenti_bursieri(300, 2);
    LOOP
        FETCH lista_studenti_bursieri INTO v_studenti_linie;
        EXIT WHEN lista_studenti_bursieri%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE(v_studenti_linie.nume || ' ' || v_studenti_linie.prenume);
    END LOOP;
    CLOSE lista_studenti_bursieri;
    */
    FOR v_studenti_linie in lista_studenti_bursieri(300, 2) LOOP
        DBMS_OUTPUT.PUT_LINE(v_studenti_linie.nume || ' ' || v_studenti_linie.prenume);
    END LOOP;
END;



/* 17 */
SET SERVEROUTPUT ON;
DECLARE 
    CURSOR update_note IS
    SELECT * FROM note FOR UPDATE OF valoare NOWAIT;
BEGIN
    FOR v_linie IN update_note LOOP
        IF (v_linie.valoare < 5) 
            THEN 
                UPDATE note SET valoare = 5 WHERE CURRENT OF update_note;
        END IF;
   END LOOP;
END;



/* 18 */
--studentul cu media sau nota la BD mai mare
SET SERVEROUTPUT ON;
DECLARE
    v_count_valoare     NUMBER              := &i_count_valoare;
    v_id1               studenti.id%TYPE    := &i_id1;
    v_id2               studenti.id%TYPE    := &i_id2;
    v_count_id1         NUMBER              DEFAULT 0;
    v_count_id2         NUMBER              DEFAULT 0;
    v_count_valoare1    NUMBER              DEFAULT 0;
    v_count_valoare2    NUMBER              DEFAULT 0;
    v_avg_valoare1      FLOAT               DEFAULT 0;
    v_avg_valoare2      FLOAT               DEFAULT 0;
    v_count_BD1         NUMBER              DEFAULT 0;
    v_count_BD2         NUMBER              DEFAULT 0;
    v_BD1               note.valoare%TYPE   DEFAULT 0;
    v_BD2               note.valoare%TYPE   DEFAULT 0;
BEGIN
    SELECT COUNT(id) INTO v_count_id1 FROM studenti WHERE id = v_id1;
    SELECT COUNT(id) INTO v_count_id2 FROM studenti WHERE id = v_id2;
    IF (v_count_id1 = 0 AND v_count_id2 = 0)
        THEN
            DBMS_OUTPUT.PUT_LINE('Nici studentul cu id-ul ' || v_id1 || ' si nici studentul cu id-ul ' || v_id2 ||
            ' nu exista in baza de date.');
    ELSIF (v_count_id1 = 0 AND v_count_id2 = 1)
        THEN
            DBMS_OUTPUT.PUT_LINE('Studentul cu id-ul ' || v_id1 || ' nu exista in baza de date.');
    ELSIF (v_count_id1 = 1 AND v_count_id2 = 0)
        THEN
            DBMS_OUTPUT.PUT_LINE('Studentul cu id-ul ' || v_id2 || ' nu exista in baza de date.');
    ELSE
        SELECT COUNT(valoare) INTO v_count_valoare1 FROM studenti s JOIN note n ON s.id = n.id_student WHERE s.id = v_id1;
        SELECT COUNT(valoare) INTO v_count_valoare2 FROM studenti s JOIN note n ON s.id = n.id_student WHERE s.id = v_id2;
        IF (v_count_valoare1 < v_count_valoare AND v_count_valoare2 < v_count_valoare)
            THEN
                DBMS_OUTPUT.PUT_LINE('Nici studentul cu id-ul ' || v_id1 || ' si nici studentul cu id-ul ' || v_id2 ||
                ' nu au minim ' || v_count_valoare || ' note.');
        ELSIF (v_count_valoare1 < v_count_valoare AND v_count_valoare2 >= v_count_valoare)
            THEN
                DBMS_OUTPUT.PUT_LINE('Studentul cu id-ul ' || v_id1 || ' nu are minim ' || v_count_valoare || ' note.');
        ELSIF (v_count_valoare1 >= v_count_valoare AND v_count_valoare2 < v_count_valoare)
            THEN
                DBMS_OUTPUT.PUT_LINE('Studentul cu id-ul ' || v_id2 || ' nu are minim ' || v_count_valoare || ' note.');
        ELSE
            SELECT AVG(valoare) INTO v_avg_valoare1 FROM studenti s JOIN note n ON s.id = n.id_student WHERE s.id = v_id1;
            SELECT AVG(valoare) INTO v_avg_valoare2 FROM studenti s JOIN note n ON s.id = n.id_student WHERE s.id = v_id2;
            IF (v_avg_valoare1 > v_avg_valoare2)
                THEN
                    DBMS_OUTPUT.PUT_LINE('Studentul cu id-ul ' || v_id1 || ' are media mai mare decat studentul cu id-ul ' ||
                    v_id2 || '.');
            ELSIF (v_avg_valoare1 < v_avg_valoare2)
                THEN
                    DBMS_OUTPUT.PUT_LINE('Studentul cu id-ul ' || v_id2 || ' are media mai mare decat studentul cu id-ul ' ||
                    v_id1 || '.');
            ELSE
                SELECT COUNT(valoare) INTO v_count_BD1 FROM studenti s JOIN note n ON s.id = n.id_student
                JOIN cursuri c ON n.id_curs = c.id WHERE s.id = v_id1 AND titlu_curs = 'Baze de date';
                SELECT COUNT(valoare) INTO v_count_BD2 FROM studenti s JOIN note n ON s.id = n.id_student
                JOIN cursuri c ON n.id_curs = c.id WHERE s.id = v_id2 AND titlu_curs = 'Baze de date';
                IF (v_count_BD1 = 0 AND v_count_BD2 = 0)
                    THEN
                        DBMS_OUTPUT.PUT_LINE('Nici studentul cu id-ul ' || v_id1 || ' si nici studentul cu id-ul ' || v_id2 ||
                        ' nu au nota la ''Baze de date''.');
                ELSIF (v_count_BD1 = 1 AND v_count_BD2 = 0)
                    THEN
                        SELECT valoare INTO v_BD1 FROM studenti s JOIN note n ON s.id = n.id_student
                        JOIN cursuri c ON n.id_curs = c.id WHERE s.id = v_id1 AND titlu_curs = 'Baze de date';
                        DBMS_OUTPUT.PUT_LINE('Studentul cu id-ul ' || v_id1 || ' are nota ' || v_BD1 ||
                        ' la ''Baze de date'', iar studentul cu id-ul' || v_id2 || ' nu are nota la ''Baze de date''.');
                ELSIF (v_count_BD1 = 0 AND v_count_BD2 = 1)
                    THEN
                        SELECT valoare INTO v_BD2 FROM studenti s JOIN note n ON s.id = n.id_student
                        JOIN cursuri c ON n.id_curs = c.id WHERE s.id = v_id2 AND titlu_curs = 'Baze de date';
                        DBMS_OUTPUT.PUT_LINE('Studentul cu id-ul ' || v_id2 || ' are nota ' || v_BD1 ||
                        ' la ''Baze de date'', iar studentul cu id-ul' || v_id1 || ' nu are nota la ''Baze de date''.');
                ELSE
                    SELECT valoare INTO v_BD1 FROM studenti s JOIN note n ON s.id = n.id_student
                    JOIN cursuri c ON n.id_curs = c.id WHERE s.id = v_id1 AND titlu_curs = 'Baze de date';
                    SELECT valoare INTO v_BD2 FROM studenti s JOIN note n ON s.id = n.id_student
                    JOIN cursuri c ON n.id_curs = c.id WHERE s.id = v_id2 AND titlu_curs = 'Baze de date';
                    IF (v_BD1 > v_BD2)
                        THEN
                            DBMS_OUTPUT.PUT_LINE('Studentul cu id-ul ' || v_id1 ||
                            ' are nota la ''Baze de date'' mai mare decat studentul cu id-ul ' ||
                            v_id2 || '.');
                    ELSIF (v_BD1 < v_BD2)
                        THEN
                            DBMS_OUTPUT.PUT_LINE('Studentul cu id-ul ' || v_id2 ||
                            ' are nota la ''Baze de date'' mai mare decat studentul cu id-ul ' ||
                            v_id1 || '.');
                    ELSE
                        DBMS_OUTPUT.PUT_LINE('Atat studentul cu id-ul ' || v_id1 ||
                        ', cat si studentul cu id-ul ' || v_id2 || ' sunt castigatori.');
                    END IF;
                END IF;
            END IF;
        END IF;
    END IF;
END;



/* 19 */
-- Fibonacci
SET SERVEROUTPUT ON;
DROP TABLE fibonacci;
CREATE TABLE fibonacci(id NUMBER, value NUMBER);
DECLARE
    v_value1  NUMBER  DEFAULT 1;
    v_value2  NUMBER  DEFAULT 1; 
    v_sum     NUMBER  DEFAULT 1;
    v_id      NUMBER   DEFAULT 1;
BEGIN
    WHILE v_sum < 1000 LOOP
        INSERT INTO fibonacci VALUES(v_id, v_sum);
        v_sum := v_value1 + v_value2;
        v_value1 := v_value2;
        v_value2 := v_sum;
        v_id := v_id + 1; 
    END LOOP;
END;
SELECT * FROM fibonacci;



/* 20 */
-- id-ul, numele si prenumele studentilor cu bursa mai mare ca 1000
SET SERVEROUTPUT ON;
CREATE OR REPLACE TYPE number_array AS TABLE OF NUMBER;
CREATE OR REPLACE TYPE varchar_array AS TABLE OF VARCHAR2(2000);
DECLARE
    v_id_array              number_array;
    v_student_nume_array    varchar_array;
    v_student_prenume_array varchar_array;
    v_student_bursa_array   number_array;
BEGIN
    SELECT id, nume, prenume, bursa
    BULK COLLECT INTO v_id_array, v_student_nume_array, v_student_prenume_array, v_student_bursa_array FROM
    (select s.id as id, s.nume as nume, s.prenume as prenume, s.bursa as bursa
    FROM studenti s WHERE bursa > 1000);
    IF v_id_array.COUNT > 0
        THEN 
            FOR i IN 1..v_id_array.COUNT LOOP
                DBMS_OUTPUT.PUT_LINE(v_id_array(i) || ' - ' || v_student_nume_array(i) || ' - ' ||
                v_student_prenume_array(i) || ' - ' || v_student_bursa_array(i));
            END LOOP;
    END IF;
END;