SET LINESIZE 200;
SET PAGESIZE 100;
/*1*/ SELECT DECODE(INITCAP(grad_didactic), 'Prof', UPPER(nume), 'Conf', UPPER(nume), INITCAP(nume)) "Grad" FROM profesori;
/*2*/ SELECT 'Cursul ' || titlu_curs || ' se preda in anul ' || DECODE(an, 1, 'unu', 2, 'doi', 3, 'trei') "Cursuri" FROM cursuri ORDER BY LENGTH(titlu_curs) DESC;
/*3*/ SELECT DISTINCT titlu_curs FROM cursuri NATURAL JOIN note WHERE TRIM(TO_CHAR(data_notare, 'D')) = 3;
/*4*/ SELECT p.nume, p.prenume, NVL(c.titlu_curs, '-') "Curs" FROM profesori p LEFT OUTER JOIN didactic d ON p.id_prof = d.id_prof LEFT OUTER JOIN cursuri c ON d.id_curs = c.id_curs;
/*5*/ SELECT s1.nume || ' ' || s1.prenume || ' - ' || s2.nume || ' ' || s2.prenume "Studenti", c1.titlu_curs, n1.valoare FROM studenti s1 JOIN studenti s2 ON s1.nume <= s2.nume AND s1.nr_matricol != s2.nr_matricol JOIN note n1 ON s1.nr_matricol = n1.nr_matricol JOIN note n2 ON s2.nr_matricol = n2.nr_matricol JOIN cursuri c1 ON n1.id_curs = c1.id_curs JOIN cursuri c2 ON n2.id_curs = c2.id_curs WHERE n1.valoare = n2.valoare AND c1.titlu_curs = c2.titlu_curs;