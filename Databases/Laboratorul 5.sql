SET LINESIZE 200;
SET PAGESIZE 100;
SELECT table_name FROM user_tables;
/*1*/ SELECT s1.nume || ' ' || s1.prenume || ' - ' || s2.nume || ' ' || s2.prenume "Studenti", s1.data_nastere || ' - ' || s2.data_nastere "Date de nastere", ABS(MONTHS_BETWEEN(s1.data_nastere, s2.data_nastere) / 12) "Diferenta de varsta" FROM studenti s1 JOIN studenti s2 ON s1.nr_matricol > s2.nr_matricol ORDER BY "Diferenta de varsta";
/*2*/ SELECT s.nume || ' ' || s.prenume || ' - ' || TRIM(p.nume) || ' ' || p.prenume "Prietenii" FROM studenti s JOIN profesori p ON LENGTH(s.nume) = LENGTH(TRIM(p.nume));
/*3*/ SELECT titlu_curs FROM cursuri NATURAL JOIN note MINUS SELECT titlu_curs FROM cursuri NATURAL JOIN note WHERE valoare > 8;
/*4*/ SELECT nume, prenume, valoare FROM studenti NATURAL JOIN note MINUS SELECT nume, prenume, valoare FROM studenti NATURAL JOIN note WHERE valoare < 7;
/*5*/ SELECT s.nume, s.prenume, n.valoare, c.titlu_curs FROM studenti s JOIN note n ON s.nr_matricol = n.nr_matricol JOIN cursuri c ON n.id_curs = c.id_curs WHERE n.valoare = ANY(7, 10) AND UPPER(c.titlu_curs) = 'OOP' AND TRIM(TO_CHAR(n.data_notare, 'Day')) = 'Tuesday';
/*6*/ SELECT nume, prenume, valoare, TRIM(TO_CHAR(data_notare, 'MONTH')) || ', ' || TO_CHAR(data_notare, 'YYYY') "Sesiune", DECODE(TO_CHAR(LAST_DAY(data_notare), 'DD'), 30, NULL, 31, NULL, '+') "Grea" FROM studenti NATURAL JOIN note WHERE valoare >= 5;