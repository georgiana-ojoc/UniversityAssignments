SET SERVEROUTPUT ON;

BEGIN
    drop_if_exists('studenti_oop', 'table');
    drop_if_exists('student_bursier', 'type');
END;
/
CREATE OR REPLACE TYPE student AS OBJECT
    (nume VARCHAR2(10),
    prenume VARCHAR2(10),
    grupa VARCHAR2(4),
    an NUMBER(1),
    data_nastere DATE,
    CONSTRUCTOR FUNCTION student(nume VARCHAR2, prenume VARCHAR2)
        RETURN SELF AS RESULT,
    -- MAP MEMBER FUNCTION varsta_in_zile RETURN NUMBER
    ORDER MEMBER FUNCTION varsta_in_zile(p_student STUDENT)
        RETURN NUMBER,
    NOT FINAL MEMBER PROCEDURE afiseaza_foaia_matricola
) /* NOT INSTANTIABLE */ NOT FINAL;
/
CREATE OR REPLACE TYPE BODY student AS
    CONSTRUCTOR FUNCTION student(nume VARCHAR2, prenume VARCHAR2)
        RETURN SELF AS RESULT AS
    BEGIN
        SELF.nume := nume;
        SELF.prenume := prenume;
        SELF.data_nastere := SYSDATE;
        SELF.an := 1;
        SELF.grupa := 'A1';
        RETURN;
    END student;
    /*
    MAP MEMBER FUNCTION varsta_in_zile RETURN NUMBER AS
    BEGIN
        RETURN SYSDATE - data_nastere;
    END varsta_in_zile;
     */
    ORDER MEMBER FUNCTION varsta_in_zile(p_student STUDENT)
        RETURN NUMBER AS
    BEGIN
        IF data_nastere > p_student.data_nastere THEN RETURN -1;
        ELSIF data_nastere = p_student.data_nastere THEN RETURN 0;
        END IF;
        RETURN 1;
    END varsta_in_zile;
    MEMBER PROCEDURE afiseaza_foaia_matricola AS
    BEGIN
        DBMS_OUTPUT.PUT_LINE(nume || ' ' || prenume || ' ' || data_nastere || ' ' || an || ' ' || grupa);
    END afiseaza_foaia_matricola;
END;
/
CREATE OR REPLACE TYPE student_bursier UNDER student
    (bursa NUMBER(6, 2),
    OVERRIDING MEMBER PROCEDURE afiseaza_foaia_matricola
);
/
CREATE OR REPLACE TYPE BODY student_bursier AS
    OVERRIDING MEMBER PROCEDURE afiseaza_foaia_matricola AS
    BEGIN
        DBMS_OUTPUT.PUT_LINE('bursier');
    END afiseaza_foaia_matricola;
END;
/
CREATE TABLE studenti_oop (nr_matricol VARCHAR2(4), obiect STUDENT);
/
DECLARE
    v_student1 STUDENT;
    v_student2 STUDENT;
    v_student3 STUDENT;
    v_student4 STUDENT_BURSIER;
    v_student5 STUDENT_BURSIER;
    v_student STUDENT;
BEGIN
    v_student1 := student('Popescu', 'Ionut', 'A2', 3, TO_DATE('11/04/1994', 'DD/MM/YYYY'));
    v_student2 := student('Vasilescu', 'George', 'A4', 3, TO_DATE('22/03/1995', 'DD/MM/YYYY'));
    v_student3 := student('Marinescu', 'Alexandru');
    v_student4 := student_bursier('Mihalcea', 'Mircea', 'A1', 2, TO_DATE('18/09/1996', 'DD/MM/YYYY'), 1000);
    v_student5 := student_bursier('Munteanu', 'David', 'B3', 3, TO_DATE('30/07/1995', 'DD/MM/YYYY'), 800);
    v_student1.afiseaza_foaia_matricola();
    v_student4.afiseaza_foaia_matricola();
    IF v_student1 < v_student2
        THEN DBMS_OUTPUT.PUT_LINE('Studentul '|| v_student1.nume || ' este mai tanar.');
        ELSE DBMS_OUTPUT.PUT_LINE('Studentul '|| v_student2.nume || ' este mai tanar.');
    END IF;
    IF v_student4 < v_student5
        THEN DBMS_OUTPUT.PUT_LINE('Studentul '|| v_student1.nume || ' este mai tanar.');
        ELSE DBMS_OUTPUT.PUT_LINE('Studentul '|| v_student2.nume || ' este mai tanar.');
    END IF;
    INSERT INTO studenti_oop VALUES ('100', v_student1);
    INSERT INTO studenti_oop VALUES ('101', v_student2);
    INSERT INTO studenti_oop VALUES ('102', v_student3);
    INSERT INTO studenti_oop VALUES ('103', v_student4);
    INSERT INTO studenti_oop VALUES ('104', v_student5);
    SELECT obiect INTO v_student FROM studenti_oop WHERE nr_matricol = '100';
    v_student.afiseaza_foaia_matricola();
END;
/
SELECT * FROM studenti_oop ORDER BY obiect;