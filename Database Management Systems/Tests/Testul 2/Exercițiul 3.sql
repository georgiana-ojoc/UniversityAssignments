SET SERVEROUTPUT ON;
DROP TABLE zodiac;
CREATE TABLE zodiac(id_zodie NUMBER(10) PRIMARY KEY, nume_zodie VARCHAR2(30),
data_inceput VARCHAR2(10), data_sfarsit VARCHAR2(10));
INSERT INTO zodiac VALUES(1, 'berbec', '21-03', '20-04');
INSERT INTO zodiac VALUES(2, 'taur', '21-04', '21-05');
INSERT INTO zodiac VALUES(3, 'gemeni', '22-05', '21-06');
INSERT INTO zodiac VALUES(4, 'rac', '22-06', '22-07');
INSERT INTO zodiac VALUES(5, 'leu', '23-07', '22-08');
INSERT INTO zodiac VALUES(6, 'fecioara', '23-08', '21-09');
INSERT INTO zodiac VALUES(7, 'balanta', '22-09', '22-10');
INSERT INTO zodiac VALUES(8, 'scorpion', '23-10', '21-11');
INSERT INTO zodiac VALUES(9, 'sagetator', '22-11', '20-12');
INSERT INTO zodiac VALUES(10, 'capricorn', '21-12', '19-01');
INSERT INTO zodiac VALUES(11, 'varsator', '20-01', '18-02');
INSERT INTO zodiac VALUES(12, 'pesti', '19-02', '20-03');
BEGIN
    FOR v_linie IN (SELECT id_zodie, COUNT(id) AS numar, nume_zodie FROM studenti JOIN zodiac ON
    (TO_DATE(TO_CHAR(data_nastere, 'DD-MM'), 'DD-MM') BETWEEN TO_DATE(data_inceput, 'DD-MM')
    AND TO_DATE(data_sfarsit, 'DD-MM'))
    OR TO_DATE(TO_CHAR(data_nastere, 'DD-MM'), 'DD-MM') >= TO_DATE('21-12', 'DD-MM')
    OR TO_DATE(TO_CHAR(data_nastere, 'DD-MM'), 'DD-MM') <= TO_DATE('19-01', 'DD-MM')
    GROUP BY id_zodie, nume_zodie ORDER BY id_zodie) LOOP
        DBMS_OUTPUT.PUT_LINE(v_linie.id_zodie || ' ' || v_linie.numar || ' ' || v_linie.nume_zodie);
    END LOOP;
END;