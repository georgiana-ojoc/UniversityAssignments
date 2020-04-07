SET SERVEROUTPUT ON;

CREATE OR REPLACE TYPE medii AS TABLE OF NUMBER(4, 2);
/
ALTER TABLE studenti ADD lista_medii medii NESTED TABLE lista_medii STORE AS lista_medii;
/
UPDATE studenti SET lista_medii = medii();
/
CREATE OR REPLACE PROCEDURE adauga_medii AS
    v_an NUMBER(1) DEFAULT 0;
    v_medie NUMBER(4, 2) DEFAULT 0;
    
    TYPE t_student IS RECORD (id studenti.id%TYPE, an studenti.an%TYPE);
    TYPE t_studenti IS TABLE OF t_student;
    v_studenti t_studenti;
BEGIN
    SELECT id, an BULK COLLECT INTO v_studenti FROM studenti;
    FOR i IN v_studenti.FIRST .. v_studenti.LAST LOOP
        FOR c_an IN 1 .. v_studenti(i).an LOOP
            FOR c_semestru IN 1 .. 2 LOOP
                SELECT AVG(valoare) INTO v_medie
                    FROM studenti s JOIN note n ON s.id = n.id_student JOIN cursuri c ON n.id_curs = c.id
                    WHERE s.id = v_studenti(i).id AND c.an = c_an AND semestru = c_semestru;
                UPDATE studenti SET lista_medii = lista_medii MULTISET UNION medii(v_medie)
                    WHERE id = v_studenti(i).id;
            END LOOP;
        END LOOP;
    END LOOP;
END;
/
CREATE OR REPLACE FUNCTION numar_medii(p_id studenti.id%TYPE) RETURN NUMBER AS
    lista_medii_student medii;
BEGIN
    SELECT lista_medii INTO lista_medii_student FROM studenti WHERE id = p_id;
    RETURN lista_medii_student.COUNT;
END;
/
BEGIN
    adauga_medii;
    DBMS_OUTPUT.PUT_LINE('Studentul cu id-ul 1 are ' || numar_medii(1) || ' note.');
END;
/
SELECT id, an, lista_medii FROM studenti ORDER BY id;
