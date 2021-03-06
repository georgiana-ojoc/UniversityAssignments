SET LINESIZE 200;
SET PAGESIZE 100;
/*1*/ SELECT an, COUNT(*) FROM studenti GROUP BY an;
/*2*/ SELECT grupa, an, COUNT(*) FROM studenti GROUP BY an, grupa ORDER BY an, grupa;
/*3*/ SELECT grupa, an, COUNT(*) "Studenti", COUNT(bursa) "Bursa" FROM studenti GROUP BY an, grupa ORDER BY an, grupa;
/*4*/ SELECT SUM(bursa) FROM studenti;
/*5*/ SELECT AVG(NVL(bursa, 0)) FROM studenti;
/*6*/ SELECT valoare, COUNT(valoare) FROM note GROUP BY valoare ORDER BY valoare DESC;
/*7*/ SELECT TO_CHAR(data_notare, 'Day') "Zi", COUNT(*) FROM note GROUP BY TO_CHAR(data_notare, 'Day') ORDER BY COUNT(*) DESC;
/*8*/ SELECT TO_CHAR(data_notare, 'D') D, COUNT(*) FROM note GROUP BY TO_CHAR(data_notare, 'D') ORDER BY TO_CHAR(data_notare, 'D');
/*9*/ SELECT nume, prenume, AVG(valoare) "Medie" FROM studenti NATURAL JOIN note GROUP BY nr_matricol, nume, prenume ORDER BY AVG(valoare) DESC;
/*10*/ SELECT nume, prenume, AVG(valoare) "Medie" FROM studenti s LEFT OUTER JOIN note n ON s.nr_matricol = n.nr_matricol GROUP BY s.nr_matricol, nume, prenume ORDER BY AVG(valoare) DESC;
/*11*/ SELECT nume, prenume, NVL(AVG(valoare), 0) "Medie" FROM studenti s LEFT OUTER JOIN note n ON s.nr_matricol = n.nr_matricol GROUP BY s.nr_matricol, nume, prenume ORDER BY NVL(AVG(valoare), 0) DESC;
/*12*/ SELECT nume, prenume, AVG(valoare) "Medie" FROM studenti NATURAL JOIN note GROUP BY nr_matricol, nume, prenume HAVING AVG(valoare) > 8 ORDER BY AVG(valoare) DESC;
/*13*/ SELECT nume, prenume, MAX(valoare) "Maxim", MIN(valoare) "Minim", AVG(valoare) "Medie" FROM studenti NATURAL JOIN note GROUP BY nr_matricol, nume, prenume HAVING MIN(valoare) >= 7;
/*14*/ SELECT nume, prenume, AVG(valoare) "Medie" FROM studenti NATURAL JOIN note GROUP BY nr_matricol, nume, prenume HAVING COUNT(valoare) >= 4;
/*15*/ SELECT nume, prenume, NVL(AVG(valoare), 0) "Medie" FROM studenti s LEFT OUTER JOIN note n ON s.nr_matricol = n.nr_matricol WHERE grupa = 'A2' AND an = 3 GROUP BY s.nr_matricol, nume, prenume;
/*16*/ SELECT MAX(AVG(valoare)) FROM note GROUP BY nr_matricol;
/*17*/ SELECT titlu_curs, MIN(valoare) "Minim", MAX(valoare) "Maxim" FROM cursuri NATURAL JOIN note GROUP BY titlu_curs;