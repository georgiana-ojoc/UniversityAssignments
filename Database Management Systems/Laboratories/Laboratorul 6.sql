/* 1 */
SET SERVEROUTPUT ON;
CREATE OR REPLACE FUNCTION nota_recenta_student(p_id studenti.id%type) RETURN VARCHAR2 AS
    nota_recenta        INTEGER;
    counter             INTEGER;
    student_inexistent  EXCEPTION;
    PRAGMA EXCEPTION_INIT(student_inexistent, -20001);
    student_fara_note   EXCEPTION;
    PRAGMA EXCEPTION_INIT(student_fara_note, -20002);
BEGIN
    SELECT COUNT(*) INTO counter FROM studenti WHERE id = p_id;
    IF counter = 0 THEN
        RAISE student_inexistent;
    ELSE
        SELECT COUNT(*) INTO counter FROM note WHERE id_student = p_id;
        IF counter = 0 THEN
            RAISE student_fara_note;
        END IF;
    END IF;
    SELECT valoare INTO nota_recenta FROM
    (SELECT valoare FROM note
    WHERE id_student = p_id
    ORDER BY data_notare DESC)
    WHERE rownum <= 1;
    RETURN 'Cea mai recenta nota a studentului cu ID-ul ' || p_id || ' este ' || nota_recenta || '.';
EXCEPTION
    WHEN student_inexistent THEN
        RAISE_APPLICATION_ERROR(-20001, 'Studentul cu ID-ul ' || p_id || ' nu exista in baza de date.');
    RETURN NULL;
    WHEN student_fara_note THEN
        RAISE_APPLICATION_ERROR(-20002,'Studentul cu ID-ul ' || p_id || ' nu are nici o nota.');
    RETURN NULL;
    WHEN OTHERS THEN
        RETURN 'Eroare neasteptata.';
END nota_recenta_student;
/
SELECT nota_recenta_student(-1) FROM DUAL;
/
SELECT nota_recenta_student(10) FROM DUAL;
/
/* 2 */
SET SERVEROUTPUT ON;
DECLARE
    teste EXCEPTION;
    PRAGMA EXCEPTION_INIT(teste, -20001);
BEGIN
    BEGIN
        RAISE teste;
        DBMS_OUTPUT.PUT_LINE('Cod ce nu se va mai executa... Se va sari la tratarea exceptiei.');
    EXCEPTION
        WHEN teste THEN
            DBMS_OUTPUT.PUT_LINE('Exceptia.');
            DBMS_OUTPUT.PUT_LINE('Cod ce se va executa... Exceptia a fost deja tratata.');
    END;
    DBMS_OUTPUT.PUT_LINE('Cod ce se va executa... Nu este in acelasi bloc cu cel in care s-a produs exceptia.');
END;
