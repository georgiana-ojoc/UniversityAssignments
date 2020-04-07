SET LINESIZE 200;
SET PAGESIZE 100;
/*B1*/
/*1*/ SELECT nume, prenume, AVG(valoare) medie FROM studenti NATURAL JOIN note GROUP BY nr_matricol, nume, prenume HAVING AVG(valoare) = (SELECT MAX(medie) FROM (SELECT AVG(valoare) medie FROM note GROUP BY nr_matricol));
/*2*/ SELECT COUNT(DISTINCT grupa) "Numar de grupe" FROM studenti;
/*3*/ SELECT nume, prenume, AVG(valoare) medie FROM studenti NATURAL JOIN note WHERE an = 2 AND valoare >= 5 GROUP BY nr_matricol, nume, prenume;
/*4*/ SELECT bursa FROM (SELECT bursa FROM studenti ORDER BY bursa DESC NULLS LAST) WHERE ROWNUM < 3;
/*5*/ ALTER TABLE profesori RENAME TO instructori;
/*6*/ UPDATE note n SET valoare = valoare * (SELECT DISTINCT credite FROM cursuri NATURAL JOIN note WHERE n.id_curs = id_curs);

/*B4*/
/*1*/ SELECT nume, prenume, AVG(valoare) medie FROM studenti NATURAL JOIN note GROUP BY nr_matricol, nume, prenume HAVING AVG(valoare) > (SELECT AVG(valoare) FROM studenti NATURAL JOIN note);
/*2*/ SELECT titlu_curs, medie FROM (SELECT titlu_curs, AVG(valoare) medie FROM cursuri NATURAL JOIN note GROUP BY id_curs, titlu_curs ORDER BY AVG(valoare) DESC, titlu_curs) WHERE ROWNUM < 3;
/*3*/ INSERT INTO studenti SELECT MAX(nr_matricol) + 10, 'Ionescu', 'Maria', 1, 'A1', NULL, TO_DATE('01.01.2000', 'DD.MM.YYYY') FROM studenti;
/*4*/ ALTER TABLE studenti ADD CONSTRAINT pk_studs PRIMARY KEY (nr_matricol); 
/*5*/ INSERT INTO studenti SELECT MAX(nr_matricol) + 10, 'Ionescu', 'Maria', 1, 'A1', NULL, TO_DATE('01.01.2000', 'DD.MM.YYYY') FROM studenti;
/*6*/ SELECT an, grupa, COUNT(nr_matricol) "Numar studenti", COUNT(bursa) "Numar bursieri" FROM studenti GROUP BY an, grupa HAVING COUNT(bursa) > 3;

/*A7*/
/*1*/ DELETE FROM studenti WHERE nr_matricol IN (SELECT nr_matricol FROM studenti NATURAL JOIN note GROUP BY nr_matricol, valoare HAVING valoare < 5);
/*2*/ UPDATE studenti s SET bursa = 50 * (SELECT AVG(valoare) FROM studenti NATURAL JOIN note WHERE s.nr_matricol = nr_matricol GROUP BY nr_matricol HAVING MIN(valoare) >= 5);
/*3*/   SELECT nume, COUNT(nume) FROM studenti GROUP BY nume HAVING COUNT(nume) > 1;
        SELECT nume || ' ' || prenume "Nume si prenume", COUNT(nume || ' ' || prenume) "Numar de aparitii" FROM studenti GROUP BY nume || ' ' || prenume HAVING COUNT(nume || ' ' || prenume) > 1;
/*4*/ SELECT an, grupa, COUNT(nr_matricol) "Numar studenti" FROM studenti GROUP BY an, grupa HAVING COUNT(nr_matricol) = (SELECT MAX(studenti) FROM (SELECT COUNT(nr_matricol) studenti FROM studenti GROUP BY an, grupa));
/*5*/ SELECT COUNT(DISTINCT nr_matricol) "Numar studenti", COUNT(DISTINCT an) "Numar ani", COUNT(DISTINCT grupa) "Numar grupe" FROM studenti;
/*6*/ SELECT nume, prenume, valoare FROM (SELECT nume, prenume, valoare FROM studenti s JOIN note n ON s.nr_matricol = n.nr_matricol JOIN cursuri c ON n.id_curs = c.id_curs WHERE titlu_curs = 'BD' ORDER BY valoare DESC) WHERE ROWNUM < 4;

/*X1*/
/*1*/ SELECT COUNT(DISTINCT TO_CHAR(data_notare, 'D')) "Numar zile saptamana" FROM note;
/*2*/ SELECT nume, prenume, COUNT(id_curs) "Numar cursuri" FROM profesori NATURAL JOIN DIDACTIC GROUP BY id_prof, nume, prenume HAVING COUNT(id_curs) > 1;
/*3*/ SELECT nume, prenume, grad_didactic FROM profesori WHERE grad_didactic = (SELECT grad_didactic FROM (SELECT * FROM profesori ORDER BY nume, prenume) WHERE ROWNUM = 1);
/*4*/ SELECT * FROM (SELECT * FROM (SELECT * FROM profesori ORDER BY nume, prenume) WHERE ROWNUM < 4 ORDER BY nume DESC, prenume DESC) WHERE ROWNUM = 1;
/*5*/ CREATE TABLE medii (nr_matricol CHAR(6) NOT NULL, medie NUMBER(4,2));
/*6*/ INSERT INTO medii SELECT nr_matricol, AVG(valoare) FROM studenti NATURAL JOIN note GROUP BY nr_matricol;