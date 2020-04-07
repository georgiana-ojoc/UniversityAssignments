SET SERVEROUTPUT ON;

CREATE OR REPLACE FUNCTION recomandari(p_id studenti.id%TYPE) RETURN VARCHAR2 AS
    student_inexistent  EXCEPTION;
    PRAGMA EXCEPTION_INIT(student_inexistent, -20001);
    prietenie_inexistenta  EXCEPTION;
    PRAGMA EXCEPTION_INIT(prietenie_inexistenta, -20002);
    TYPE t_prieten IS RECORD (id studenti.id%TYPE, nume studenti.nume%TYPE, prenume studenti.prenume%TYPE, grad NUMBER);
    TYPE t_prieteni IS TABLE OF t_prieten;
    v_recomandari       t_prieteni;
    v_student_array     VARCHAR2(1000);
    v_count             NUMBER DEFAULT 0;
BEGIN
    SELECT COUNT(*) INTO v_count FROM studenti WHERE id = p_id;
    IF v_count = 0 THEN
        RAISE student_inexistent;
    END IF;
    v_count := 0;
    SELECT COUNT(*) INTO v_count FROM prieteni WHERE id_student1 = p_id OR id_student2 = p_id;
    IF v_count = 0 THEN
        RAISE prietenie_inexistenta;
    END IF;
    SELECT id_student, nume, prenume, rang
    BULK COLLECT INTO v_recomandari FROM
    (SELECT id_student, COUNT(id_student) rang FROM
    (SELECT p2.id_student2 id_student FROM prieteni p1 JOIN prieteni p2 ON (p1.id_student2 = p2.id_student1 AND p1.id_student1 = p_id)
    UNION ALL
    SELECT p2.id_student1 id_student FROM prieteni p1 JOIN prieteni p2 ON (p1.id_student2 = p2.id_student2 AND p1.id_student1 = p_id)
    UNION ALL
    SELECT p2.id_student2 id_student FROM prieteni p1 JOIN prieteni p2 ON (p1.id_student1 = p2.id_student1 AND p1.id_student2 = p_id)
    UNION ALL
    SELECT p2.id_student1 id_student FROM prieteni p1 JOIN prieteni p2 ON (p1.id_student1 = p2.id_student2 AND p1.id_student2 = p_id))
    WHERE id_student != p_id AND
    id_student NOT IN
    (SELECT id_student1 FROM prieteni WHERE id_student2 = p_id
    UNION
    SELECT id_student2 id FROM prieteni WHERE id_student1 = p_id)
    GROUP BY id_student
    ORDER BY COUNT(id_student) DESC)
    JOIN studenti s ON id_student = s.id
    WHERE ROWNUM < 6;
    v_student_array := '[';
    FOR i IN v_recomandari.FIRST .. v_recomandari.LAST LOOP
        v_student_array := v_student_array || JSON_OBJECT('ID' VALUE v_recomandari(i).id, 'nume' VALUE v_recomandari(i).nume,
            'prenume' VALUE v_recomandari(i).prenume, 'numar de prieteni comuni' VALUE v_recomandari(i).grad FORMAT JSON) || ',';
    END LOOP;
    DBMS_LOB.TRIM(v_student_array, DBMS_LOB.GETLENGTH(v_student_array) - 1);
    v_student_array := v_student_array || ']';
    RETURN v_student_array;
EXCEPTION
    WHEN student_inexistent THEN
        RAISE_APPLICATION_ERROR(-20001, 'Studentul cu id-ul' || p_id || ' nu exista in baza de date.');
        RETURN NULL;
    WHEN prietenie_inexistenta THEN
        RAISE_APPLICATION_ERROR(-20002, 'Studentul cu id-ul' || p_id || ' nu are nicio prietenie in baza de date.');
        RETURN NULL;
END recomandari;
/
DECLARE
    v_id studenti.id%TYPE;
BEGIN
    SELECT MIN(id) INTO v_id FROM studenti;
    DBMS_OUTPUT.PUT_LINE(recomandari(v_id));
END;
