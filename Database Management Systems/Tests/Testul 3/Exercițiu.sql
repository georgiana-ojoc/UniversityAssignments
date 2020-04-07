SET SERVEROUTPUT ON;

CREATE OR REPLACE TYPE curs_object AS OBJECT(
    curs_id NUMBER,
    curs_titlu_curs VARCHAR2(1000),
    curs_numar_note NUMBER
);

CREATE OR REPLACE TYPE curs_table AS TABLE OF curs_object;

CREATE OR REPLACE PACKAGE student AS
    PROCEDURE adauga_prietenie(p_id1 studenti.id%TYPE, p_id2 studenti.id%TYPE);
    FUNCTION get_curs(p_an cursuri.an%TYPE) RETURN curs_table;
END;

CREATE OR REPLACE PACKAGE BODY student AS
    PROCEDURE adauga_prietenie(p_id1 studenti.id%TYPE, p_id2 studenti.id%TYPE) AS
        v_exista NUMBER;
    BEGIN
        SELECT COUNT(*) INTO v_exista FROM studenti WHERE id = p_id1;
        IF v_exista = 0
            THEN
                DBMS_OUTPUT.PUT_LINE('Studentul cu id-ul ' || p_id1 || ' nu exista in baza de date.');
            ELSE
                SELECT COUNT(*) INTO v_exista FROM studenti WHERE id = p_id2;
                IF v_exista = 0
                    THEN
                        DBMS_OUTPUT.PUT_LINE('Studentul cu id-ul ' || p_id2 || ' nu exista in baza de date.');
                    ELSE
                        SELECT COUNT(*) INTO v_exista FROM prieteni WHERE id_student1 = p_id1 AND id_student2 = p_id2;
                        IF v_exista = 1
                            THEN
                                DBMS_OUTPUT.PUT_LINE('Studentii cu id-urile ' || p_id1 || ' si ' || p_id2
                                || ' sunt deja prieteni.');
                            ELSE
                                INSERT INTO prieteni(id, id_student1, id_student2) SELECT (SELECT MAX(id) + 1 FROM prieteni),
                                p_id1, p_id2 FROM dual;
                        END IF;
                END IF;
        END IF;
    END;
    
    FUNCTION get_curs(p_an cursuri.an%TYPE) RETURN curs_table AS
        v_exista NUMBER;
        curs_table_array curs_table := curs_table();
    BEGIN
        SELECT COUNT(*) INTO v_exista FROM cursuri WHERE an = p_an;
        IF v_exista = 0
            THEN
                SELECT curs_object(id, titlu_curs, numar_note) BULK COLLECT INTO curs_table_array FROM
                (SELECT id AS id, titlu_curs AS titlu_curs, numar_note AS numar_note FROM
                (SELECT c.id, titlu_curs, COUNT(valoare) numar_note FROM cursuri c JOIN note n
                ON c.id = n.id_curs WHERE an = p_an GROUP BY c.id, titlu_curs ORDER BY COUNT(valoare)) WHERE ROWNUM = 1);
            ELSE
                SELECT curs_object(id, titlu_curs, numar_note) BULK COLLECT INTO curs_table_array FROM
                (SELECT c.id AS id, titlu_curs AS titlu_curs, COUNT(valoare) AS numar_note
                FROM cursuri c JOIN note n ON c.id = n.id_curs WHERE c.id = 
                (SELECT id FROM
                (SELECT c.id FROM cursuri c JOIN note n ON c.id = n.id_curs WHERE an = p_an AND valoare = 10 GROUP BY c.id
                ORDER BY COUNT(valoare))
                WHERE ROWNUM = 1)
                GROUP BY c.id, titlu_curs);
            END IF;
        RETURN curs_table_array;
    END;
END;

BEGIN
    adauga_prietenie(1, 2);
    
END;