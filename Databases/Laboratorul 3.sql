-- TRUNC DAY - LAST SUNDAY
SET LINESIZE 200;
SET PAGESIZE 100;
/*1*/ SELECT SYSDATE AS "Astazi" FROM DUAL;
/*2*/ SELECT nume, prenume, data_nastere, MONTHS_BETWEEN(SYSDATE, data_nastere) AS "Luni diferenta" FROM studenti;
/*3*/ SELECT nume, prenume, TO_CHAR(data_nastere, 'Day', 'NLS_DATE_LANGUAGE = romanian') AS "Zi din saptamana" FROM studenti;
/*4*/ SELECT CONCAT(CONCAT(CONCAT('Elevul ', prenume), ' este in grupa '), grupa) AS "Elev si grupa" FROM studenti;
/*5*/ SELECT RPAD(CONCAT(bursa, ' '), 10, '$') AS "Bursa" FROM studenti WHERE bursa IS NOT NULL;
/*6*/ SELECT CONCAT('b', UPPER(SUBSTR(nume, 2))) AS "Nume", LENGTH(nume) AS "Lungimea numelui" FROM studenti WHERE INITCAP(nume) LIKE 'B%';
/*7*/ SELECT nume, prenume, data_nastere, ADD_MONTHS(data_nastere, TRUNC(MONTHS_BETWEEN(SYSDATE, data_nastere) / 12 + 1) * 12) AS "Zi de nastere", NEXT_DAY(ADD_MONTHS(data_nastere, TRUNC(MONTHS_BETWEEN(SYSDATE, data_nastere) / 12 + 1) * 12), 'SUNDAY') AS "Duminica" FROM studenti;
/*8*/ SELECT nume, prenume, TO_CHAR(data_nastere, 'Month', 'NLS_DATE_LANGUAGE = romanian') AS "Luna" FROM studenti WHERE bursa IS NOT NULL ORDER BY TO_CHAR(data_nastere, 'MM');
/*9*/ SELECT nume, prenume, bursa, DECODE(bursa, 450, 'premiul 1', 350, 'premiul 2', 250, 'premiul 3', 'mentiune') AS "Premiu" FROM studenti;
/*10*/ SELECT TRANSLATE(nume, 'ai', 'ia') AS "Nume nou" FROM studenti;
/*11*/ SELECT nume, prenume, data_nastere, TRUNC(MONTHS_BETWEEN(SYSDATE, data_nastere) / 12) || ' de ani, ' || MOD(FLOOR(MONTHS_BETWEEN(SYSDATE, data_nastere)), 12) || ' luni si ' || FLOOR(SYSDATE - ADD_MONTHS(data_nastere, TRUNC(MONTHS_BETWEEN(SYSDATE, data_nastere)))) || ' zile' AS "Varsta", TRUNC(ADD_MONTHS(data_nastere, TRUNC(MONTHS_BETWEEN(SYSDATE, data_nastere) / 12 + 1) * 12) - SYSDATE) AS "Zile ramase" FROM studenti;
/*12*/ SELECT nume, prenume, ROUND(SYSDATE, 'Month') AS "Luna urmatoare", DECODE(bursa, 450, 450 * 1.1, 350, 350 * 1.15, 250, 250 * 1.2, 0) AS "Bursa marita" FROM studenti;
/*13*/ SELECT nume, prenume, LPAD(RPAD(' ', bursa / 50 + 1, '*'), 10, ' ') AS "Bursa" FROM studenti WHERE bursa IS NOT NULL;