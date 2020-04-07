/* 1 */
SET SERVEROUTPUT ON;
DECLARE 
    TYPE assoc_array IS TABLE OF NUMBER INDEX BY VARCHAR2(10);
    varsta assoc_array;
BEGIN
    varsta('Gigel') := 3;
    varsta('Ionel') := 4;
    DBMS_OUTPUT.PUT_LINE('Varsta lui Gigel este ' || varsta('Gigel'));
    DBMS_OUTPUT.PUT_LINE('Varsta lui Ionel este ' || varsta('Ionel'));
END;



/* 2 */
SET SERVEROUTPUT ON;
DECLARE 
    TYPE assoc_array IS TABLE OF NUMBER INDEX BY VARCHAR2(20);
    linii assoc_array;  
BEGIN
    linii(SYSDATE) := 123;
    DBMS_OUTPUT.PUT_LINE(linii(sysdate));
END;



/* 3 */
SET SERVEROUTPUT ON;
DECLARE 
    TYPE assoc_array IS TABLE OF NUMBER INDEX BY VARCHAR2(10);
    varsta assoc_array;
BEGIN
    varsta('Gigel') := 3;
    varsta('Ionel') := 4;
    varsta('Maria') := 6;
   
    DBMS_OUTPUT.PUT_LINE('Numar de elemente in lista: ' || varsta.COUNT);
   
    DBMS_OUTPUT.PUT_LINE('Prima cheie din lista: ' || varsta.FIRST);
    DBMS_OUTPUT.PUT_LINE('Ultima cheie din lista: ' || varsta.LAST);

    DBMS_OUTPUT.PUT_LINE('Inaintea lui Ionel in lista: ' || varsta.PRIOR('Ionel'));
    DBMS_OUTPUT.PUT_LINE('Dupa Ionel in lista: ' || varsta.NEXT('Ionel'));
      
    varsta.DELETE('Maria');   
    DBMS_OUTPUT.PUT_LINE('Dupa Ionel in lista: ' || varsta.NEXT('Ionel'));   
END;



/* 4 */
SET SERVEROUTPUT ON;
DECLARE
    TYPE prenume IS TABLE OF VARCHAR2(10);
    student prenume;
BEGIN
    student := prenume('Gigel', 'Ionel');
    FOR i IN student.FIRST .. student.LAST LOOP
            DBMS_OUTPUT.PUT_LINE(i || ' - ' || student(i));    
    END LOOP;
END;



/* 5 */
SET SERVEROUTPUT ON;
DECLARE
    TYPE prenume IS TABLE OF VARCHAR2(10);
    student prenume;
BEGIN
    student := prenume('Gigel', 'Ionel', 'Maria');  
    student.EXTEND(4, 2);
    student.DELETE(2);
    FOR i IN student.FIRST .. student.LAST LOOP
        IF student.EXISTS(i) THEN
            DBMS_OUTPUT.PUT_LINE(i || ' - ' || student(i));
        END IF;
    END LOOP;
END;



/* 6 */
SET SERVEROUTPUT ON;
DECLARE
    TYPE prenume IS TABLE OF VARCHAR2(10);
    student prenume := prenume();
BEGIN  
    student.EXTEND;
END;



/* 7 */
SET SERVEROUTPUT ON;
DECLARE 
    TYPE linie_student IS TABLE OF studenti%ROWTYPE;
    lista_studenti linie_student;
BEGIN
    SELECT * BULK COLLECT INTO lista_studenti FROM studenti ORDER BY id;
    FOR i IN lista_studenti.FIRST .. lista_studenti.LAST LOOP
        IF lista_studenti.EXISTS(i) THEN
            DBMS_OUTPUT.PUT_LINE(i || ' - ' || lista_studenti(i).nume);
        END IF;
    END LOOP;   
    DBMS_OUTPUT.PUT_LINE('Numar studenti: ' || lista_studenti.COUNT);
END;



/* 8 */
SET SERVEROUTPUT ON;
DECLARE 
    TYPE linie_student IS TABLE OF studenti%ROWTYPE;
    lista_studenti linie_student;
    i VARCHAR2(100);
BEGIN
    SELECT * BULK COLLECT INTO lista_studenti FROM studenti ORDER BY id;
    i := lista_studenti.FIRST;
    WHILE i IS NOT NULL LOOP
        DBMS_OUTPUT.PUT_LINE(i || ' - ' || lista_studenti(i).nume);
        i := lista_studenti.NEXT(i);
    END LOOP;   
    DBMS_OUTPUT.PUT_LINE('Numar studenti: ' || lista_studenti.COUNT);
END;



/* 9 */
SET SERVEROUTPUT ON;

CONNECT SYS AS SYSDBA;
GRANT CREATE TYPE TO STUDENT;

CREATE OR REPLACE TYPE lista_prenume AS TABLE OF VARCHAR2(10);

CREATE TABLE persoane (nume VARCHAR2(10), prenume lista_prenume) NESTED TABLE prenume STORE AS prenume;

INSERT INTO persoane VALUES('Popescu', lista_prenume('Ionut', 'Razvan'));
INSERT INTO persoane VALUES('Ionescu', lista_prenume('Elena', 'Madalina'));
INSERT INTO persoane VALUES('Rizea', lista_prenume('Mircea', 'Catalin'));

SELECT * FROM persoane;

DECLARE    
    sir_prenume persoane.prenume%TYPE;
BEGIN
    sir_prenume := lista_prenume('Cristi', 'Tudor', 'Virgil');
    INSERT INTO persoane VALUES ('Gurau', sir_prenume);
END;

SELECT * FROM persoane;


/* 10 */
SET SERVEROUTPUT ON;
DECLARE
    TYPE var_arr IS VARYING ARRAY (5) OF VARCHAR2(10);
    orase var_arr;
BEGIN
    orase := var_arr('Iasi', 'Bacau', 'Suceava', 'Botosani');
    DBMS_OUTPUT.PUT_LINE('Limita orase: ' || orase.LIMIT);
    orase.TRIM;
    FOR i IN orase.FIRST .. orase.LAST LOOP
        DBMS_OUTPUT.PUT_LINE(orase(i));
    END LOOP;
  
    orase.EXTEND(2);
    orase(4) := 'Sibiu';
    orase(5) := 'Brasov';
    DBMS_OUTPUT.PUT_LINE('Dupa adaugare:');
    FOR i IN orase.FIRST .. orase.LAST LOOP
        DBMS_OUTPUT.PUT_LINE(orase(i));
    END LOOP;  
END;



/* 11 */
SET SERVEROUTPUT ON;

CREATE TABLE minions (culoare VARCHAR2(20), numar_ochi NUMBER(3), nume VARCHAR2(20));

DECLARE
    TYPE minion IS RECORD (culoare VARCHAR2(20) := 'Galben', numar_ochi NUMBER(3), nume VARCHAR2(20));
    v_minion minion;   
BEGIN      
    v_minion.numar_ochi := 2;
    v_minion.nume := 'Kevin';
    v_minion.numar_ochi := 1;
    v_minion.nume := 'Stuart';
    INSERT INTO MINIONS VALUES v_minion;
    DBMS_OUTPUT.PUT_LINE(v_minion.culoare);
END;

SELECT * FROM minions;



/* 12 */
SET SERVEROUTPUT ON;

CREATE OR REPLACE TYPE student_bursa AS OBJECT (id_student INT, procent_marire NUMBER);
CREATE OR REPLACE TYPE studenti_burse AS TABLE OF student_bursa;

CREATE OR REPLACE PROCEDURE mareste_bursa(p_studenti studenti_burse) AS
BEGIN
    FOR i IN (SELECT id_student, procent_marire FROM TABLE(p_studenti)) LOOP
        UPDATE studenti SET bursa = NVL(bursa, 100) * (1 + i.procent_marire) WHERE id = i.id_student;
    END LOOP;
END mareste_bursa;

SELECT id, bursa FROM studenti ORDER BY id;

BEGIN 
    mareste_bursa(studenti_burse(student_bursa(1, 0.5),student_bursa(2, 2)));
END;

SELECT id, bursa FROM studenti ORDER BY id;
ROLLBACK;
SELECT id, bursa FROM studenti ORDER BY id;



/* 13 */
SET SERVEROUTPUT ON;

CREATE OR REPLACE TYPE istoric_burse AS TABLE OF NUMBER(6, 2);

ALTER TABLE studenti ADD bursa_veche istoric_burse NESTED TABLE bursa_veche STORE AS bursa_veche;
UPDATE studenti SET bursa_veche = istoric_burse();

SELECT * FROM studenti ORDER BY id;

CREATE OR REPLACE PROCEDURE mareste_bursa(p_studenti studenti_burse) AS
BEGIN
    FOR i IN (SELECT id_student, procent_marire FROM TABLE(p_studenti)) LOOP
        UPDATE studenti SET bursa_veche = bursa_veche MULTISET UNION istoric_burse(NVL(bursa, 0)),
        bursa = NVL(bursa, 100) * (1 + i.procent_marire) WHERE id = i.id_student;
    END LOOP;
END mareste_bursa;

BEGIN 
    mareste_bursa(studenti_burse(student_bursa(1, 0.5), student_bursa(2, 2)));
END;

SELECT * FROM studenti WHERE id IN (1, 2) ORDER BY id;



/* 14 */
SET SERVEROUTPUT ON;

CREATE OR REPLACE TYPE student_object AS OBJECT (id studenti.id%TYPE, nume VARCHAR2(15), prenume VARCHAR2(30), an NUMBER(1));
CREATE OR REPLACE TYPE student_table AS TABLE OF student_object;

CREATE OR REPLACE PROCEDURE show_averages(p_students student_table) AS
    v_average NUMBER(4, 2) := 0;
    i VARCHAR2(100);
BEGIN
    i := p_students.FIRST;
    WHILE i IS NOT NULL LOOP
        IF p_students(i).an = 2 OR p_students(i).an = 3 THEN
            SELECT AVG(valoare) INTO v_average FROM note WHERE id_student = p_students(i).id;
            DBMS_OUTPUT.PUT_LINE('Studentul ' || p_students(i).nume || ' ' || p_students(i).prenume
            || ' are media ' || v_average || '.');
        END IF;
        i := p_students.NEXT(i);
    END LOOP;
END;

DECLARE
    TYPE id_var_arr IS VARYING ARRAY (3) OF studenti.id%TYPE;
    v_ids id_var_arr;
    v_students student_table;
BEGIN
    SELECT id BULK COLLECT INTO v_ids FROM (SELECT id FROM studenti WHERE an IN (2, 3) ORDER BY id) WHERE ROWNUM < 4;
    v_students := student_table();
    v_students.EXTEND(3);
    FOR i IN 1 .. 3 LOOP
        SELECT student_object (id, nume, prenume, an) INTO v_students(i) FROM studenti WHERE id = v_ids(i);
    END LOOP;
    show_averages(v_students);
END;