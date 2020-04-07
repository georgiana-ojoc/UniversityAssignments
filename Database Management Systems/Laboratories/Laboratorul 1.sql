/* 3 */
-- afisare note student
SELECT s.id, s.nume, s.prenume, c.id, c.titlu_curs, n.valoare
FROM studenti s JOIN note n ON s.id = n.id_student JOIN cursuri c ON n.id_curs = c.id WHERE s.id = 100;

-- stergere constraint (daca exista)
ALTER TABLE note DROP CONSTRAINT un_valoare_curs;

-- adaugare constraint ca un student sa nu aiba doua note la aceeasi materie
ALTER TABLE note ADD CONSTRAINT un_valoare_curs UNIQUE(id_student, id_curs);

-- incercare adaugare duplicat
INSERT INTO note(id, id_student, id_curs, valoare) SELECT (SELECT MAX(id) + 1 FROM note), 100, 1, 5 FROM dual;

-- stergere constraint
ALTER TABLE note DROP CONSTRAINT un_valoare_curs;

-- adaugare duplicat
INSERT INTO note(id, id_student, id_curs, valoare) SELECT (SELECT MAX(id) + 1 FROM note), 100, 1, 5 FROM dual;

-- incercare adaugare constraint ca un student sa nu aiba doua note la aceeasi materie
ALTER TABLE note ADD CONSTRAINT un_valoare_curs UNIQUE(id_student, id_curs);

-- stergere duplicat
DELETE FROM note WHERE id = (SELECT MAX(id) FROM note);



/* 4 */
-- selectare grupe cu coeziunea maxima
SELECT s1.an, s1.grupa, COUNT(DISTINCT s1.id) AS nr_studenti, COUNT(p.id_student1) AS nr_prietenii,
TRUNC(COUNT(p.id_student1) / COUNT(DISTINCT s1.id), 2) AS coeziune
FROM studenti s1 JOIN prieteni p ON s1.id = p.id_student1 JOIN studenti s2 ON p.id_student2 = s2.id
GROUP BY s1.an, s1.grupa
HAVING COUNT(p.id_student1) / COUNT(DISTINCT s1.id) =
(SELECT MAX(COUNT(p.id_student1) / COUNT(DISTINCT s1.id))
FROM studenti s1 JOIN prieteni p ON s1.id = p.id_student1 JOIN studenti s2 ON p.id_student2 = s2.id
GROUP BY s1.an, s1.grupa)
ORDER BY 1, 2 DESC;



/* 5 */
-- adaugare studenta
INSERT INTO studenti(id, nr_matricol, nume, prenume, an, grupa)
SELECT (SELECT MAX(id) + 1 FROM studenti), '123AB1', 'Popescu', 'Crina-Nicoleta', 2, 'B2' FROM dual;

-- afisare studenta
SELECT * FROM studenti WHERE id = (SELECT MAX(id) FROM studenti);

-- adaugare nota
INSERT INTO note(id, id_student, id_curs, valoare)
SELECT (SELECT MAX(id) + 1 FROM note), (SELECT id FROM studenti WHERE nume = 'Popescu' AND prenume = 'Crina-Nicoleta'),
(SELECT id FROM cursuri WHERE titlu_curs = 'Baze de date'), 10 FROM dual;

-- afisare nota
SELECT * FROM note WHERE id = (SELECT MAX(id) FROM note);



 /* 6 */
-- inserare prima prietenie
INSERT INTO prieteni(id, id_student1, id_student2) 
SELECT (SELECT MAX(id) + 1 FROM prieteni), (SELECT id FROM studenti WHERE nume = 'Popescu' AND prenume = 'Crina-Nicoleta'),
(SELECT MIN(id) FROM studenti WHERE an = (SELECT an FROM studenti WHERE nume = 'Popescu' AND prenume = 'Crina-Nicoleta')
AND grupa = (SELECT grupa FROM studenti WHERE nume = 'Popescu' AND prenume = 'Crina-Nicoleta') AND prenume LIKE '%a') FROM dual;

-- inserare a doua prietenie
INSERT INTO prieteni(id, id_student1, id_student2) 
SELECT (SELECT MAX(id) + 1 FROM prieteni), (SELECT id FROM studenti WHERE nume = 'Popescu' AND prenume = 'Crina-Nicoleta'),
(SELECT MIN(id) + 1 FROM studenti WHERE an = (SELECT an FROM studenti WHERE nume = 'Popescu' AND prenume = 'Crina-Nicoleta')
AND grupa = (SELECT grupa FROM studenti WHERE nume = 'Popescu' AND prenume = 'Crina-Nicoleta') AND prenume LIKE '%a') FROM dual;

-- afisare prietenii
SELECT s1.nume || ' ' || s1. prenume || ' este prietena cu ' || s2.nume || ' ' || s2.prenume || '.' AS prietenii
FROM studenti s1 JOIN prieteni p ON s1.id = p.id_student1 JOIN studenti s2 ON p.id_student2 = s2.id
WHERE s1.nume = 'Popescu' AND s1.prenume = 'Crina-Nicoleta'
ORDER BY s2.nume, s2.prenume;



/* 7 */
-- afisare prietena
SELECT id, nume, prenume FROM studenti WHERE id =
(SELECT MIN(s2.id) FROM studenti s1 JOIN prieteni p ON s1.id = p.id_student1 JOIN studenti s2 ON p.id_student2 = s2.id
WHERE s1.nume = 'Popescu' AND s1.prenume = 'Crina-Nicoleta');

-- stergere note prietena
DELETE FROM note WHERE id_student =
(SELECT MIN(s2.id) FROM studenti s1 JOIN prieteni p ON s1.id = p.id_student1 JOIN studenti s2 ON p.id_student2 = s2.id
WHERE s1.nume = 'Popescu' AND s1.prenume = 'Crina-Nicoleta');

-- stergere prieteni prietena
DELETE FROM prieteni WHERE id_student1 =
(SELECT MIN(s2.id) FROM studenti s1 JOIN prieteni p ON s1.id = p.id_student1 JOIN studenti s2 ON p.id_student2 = s2.id
WHERE s1.nume = 'Popescu' AND s1.prenume = 'Crina-Nicoleta')
OR id_student2 =
(SELECT MIN(s2.id) FROM studenti s1 JOIN prieteni p ON s1.id = p.id_student1 JOIN studenti s2 ON p.id_student2 = s2.id
WHERE s1.nume = 'Popescu' AND s1.prenume = 'Crina-Nicoleta');

-- stergere prietena
DELETE FROM studenti WHERE id =
(SELECT MIN(id) FROM studenti WHERE an = (SELECT an FROM studenti WHERE nume = 'Popescu' AND prenume = 'Crina-Nicoleta')
AND grupa = (SELECT grupa FROM studenti WHERE nume = 'Popescu' AND prenume = 'Crina-Nicoleta') AND prenume like '%a');



/* 8 */
SELECT nume, prenume, an, grupa, bursa FROM studenti WHERE bursa > 1350 ORDER BY 5 DESC, 3, 4, 1, 2;



/* 9 */
-- afisare grupe cu cei mai multi restantieri
SELECT an, grupa, COUNT(DISTINCT s.id) restantieri FROM studenti s JOIN note n on s.id = n.id_student WHERE valoare <= 4
GROUP BY an, grupa HAVING COUNT(DISTINCT s.id) =
(SELECT MAX(COUNT(DISTINCT s.id)) FROM studenti s JOIN note n on s.id = n.id_student WHERE valoare <= 4 GROUP BY an, grupa)
ORDER BY an, grupa;



/* 10 */
DROP VIEW bursieri_fruntasi;

CREATE VIEW bursieri_fruntasi AS SELECT * FROM studenti WHERE BURSA > 1350 ORDER BY id /* WITH CHECK OPTION */;

SELECT * FROM bursieri_fruntasi ORDER BY id;



/* 11 */
UPDATE studenti SET bursa = 1400 WHERE id = 1;

SELECT * FROM bursieri_fruntasi ORDER BY id;

UPDATE studenti SET bursa = 1200 WHERE id = 1;

SELECT * FROM bursieri_fruntasi ORDER BY id;

UPDATE bursieri_fruntasi SET bursa = 1400 WHERE id = 1;

SELECT * FROM bursieri_fruntasi ORDER BY id;



/* 12 */
INSERT INTO bursieri_fruntasi SELECT (SELECT MAX(id) + 1 FROM studenti),
'123AB2', 'Ionescu', 'Maria', 3, 'B1', 200, SYSDATE, 'maria.ionescu@gmail.com', SYSDATE, SYSDATE FROM dual;

INSERT INTO bursieri_fruntasi SELECT (SELECT MAX(id) + 1 FROM studenti),
'123AB3', 'Vasilescu', 'David', 2, 'B2', 1400, SYSDATE, 'david.vasilescu@gmail.com', SYSDATE, SYSDATE FROM dual;

SELECT * FROM bursieri_fruntasi WHERE id = (SELECT MAX(id) - 1 FROM studenti) ORDER BY id;

SELECT * FROM studenti WHERE id >= (SELECT MAX(id) - 1 FROM studenti) ORDER BY id;

DELETE FROM bursieri_fruntasi WHERE id >= (SELECT MAX(id) - 1 FROM studenti);

SELECT * FROM studenti WHERE id >= (SELECT MAX(id) - 1 FROM studenti) ORDER BY id;

DELETE FROM studenti WHERE id = (SELECT MAX(id) FROM studenti);



/* 13 */
DESCRIBE user_views;

SELECT view_name, text FROM user_views;



/* 14 */
DROP VIEW logica_2_B3;

CREATE VIEW logica_2_B3 AS SELECT nume, prenume, valoare FROM studenti s JOIN note n ON s.id = n.id_student
JOIN cursuri c ON n.id_curs = c.id WHERE titlu_curs = 'Logica' AND s.an = 1 AND grupa = 'B3' ORDER BY nume, prenume, valoare;

SELECT * FROM logica_2_B3 ORDER BY nume, prenume, valoare;

UPDATE logica_2_B3 SET valoare = 10 WHERE (nume, prenume) =
(SELECT nume, prenume FROM (SELECT * FROM logica_2_B3 ORDER BY nume, prenume) WHERE ROWNUM = 1);

SELECT * FROM logica_2_B3 ORDER BY nume, prenume, valoare;

INSERT INTO logica_2_B3 VALUES('Marinescu', 'Sorina', 8);
