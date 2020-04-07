SET LINESIZE 200;
SET PAGESIZE 100;

DROP TABLE studenti CASCADE CONSTRAINTS PURGE;
DROP TABLE cursuri CASCADE CONSTRAINTS PURGE;
DROP TABLE note CASCADE CONSTRAINTS PURGE;
DROP TABLE profesori CASCADE CONSTRAINTS PURGE;
DROP TABLE didactic CASCADE CONSTRAINTS PURGE;

CREATE TABLE studenti
(nr_matricol CHAR(6) NOT NULL,
nume VARCHAR2(10),
prenume VARCHAR2(10),
an NUMBER(1),
grupa CHAR(2),
bursa NUMBER(6,2),
data_nastere DATE);

CREATE TABLE cursuri
(id_curs CHAR(4) NOT NULL,
titlu_curs VARCHAR2(10),
an NUMBER(1),
semestru NUMBER(1),
credite NUMBER(2));

CREATE TABLE note
(nr_matricol CHAR(6) NOT NULL,
id_curs CHAR(4) NOT NULL,
valoare NUMBER(2),
data_notare DATE);

CREATE TABLE profesori
(id_prof CHAR(4) NOT NULL,
nume CHAR(10),
prenume CHAR(10),
grad_didactic VARCHAR2(5));

CREATE TABLE didactic
(id_prof CHAR(4) NOT NULL,
id_curs CHAR(4) NOT NULL);

INSERT INTO studenti VALUES ('1', 'Popescu', 'Bogdan', 3, 'A2', NULL, TO_DATE('17/02/1995', 'dd/mm/yyyy'));
INSERT INTO studenti VALUES ('2', 'Prelipcean', 'Radu', 3, 'A2',NULL, TO_DATE('26/05/1995', 'dd/mm/yyyy'));
INSERT INTO studenti VALUES ('3', 'Antonie', 'Ioana', 3, 'B2', 450, TO_DATE('3/01/1995', 'dd/mm/yyyy'));
INSERT INTO studenti VALUES ('4', 'Arhire', 'Raluca', 3, 'A4', NULL, TO_DATE('26/12/1995', 'dd/mm/yyyy'));
INSERT INTO studenti VALUES ('5', 'Panaite', 'Alexandru', 3, 'B3', NULL, TO_DATE('13/04/1995', 'dd/mm/yyyy'));

INSERT INTO studenti VALUES ('6', 'Bodnar', 'Ioana', 2, 'A1', NULL, TO_DATE('26/08/1996', 'dd/mm/yyyy'));
INSERT INTO studenti VALUES ('7', 'Arhip', 'Andrada', 2, 'B2', 350, TO_DATE('03/04/1996', 'dd/mm/yyyy'));
INSERT INTO studenti VALUES ('8', 'Ciobotariu', 'Ciprian', 2, 'A3', 350, TO_DATE('03/04/1996', 'dd/mm/yyyy'));
INSERT INTO studenti VALUES ('9', 'Bodnar', 'Ioana', 2, 'A4', NULL, TO_DATE('10/06/1996', 'dd/mm/yyyy'));

INSERT INTO studenti VALUES ('10', 'Pintescu', 'Andrei', 1, 'B1', 250, TO_DATE('26/08/1997', 'dd/mm/yyyy'));
INSERT INTO studenti VALUES ('11', 'Arhire', 'Alexandra', 1, 'B1', NULL, TO_DATE('02/07/1997', 'dd/mm/yyyy'));
INSERT INTO studenti VALUES ('12', 'Cobzaru', 'George', 1, 'B1', 350, TO_DATE('29/04/1997', 'dd/mm/yyyy'));
INSERT INTO studenti VALUES ('13', 'Bucur', 'Andreea', 1, 'B2', NULL, TO_DATE('10/05/1997', 'dd/mm/yyyy'));

INSERT INTO cursuri VALUES ('1', 'Logica', 1, 1, 6);
INSERT INTO cursuri VALUES ('2', 'Matematica', 1, 1, 5);
INSERT INTO cursuri VALUES ('3', 'POO', 1, 2, 6);
INSERT INTO cursuri VALUES ('4', 'BD', 2, 1, 6);
INSERT INTO cursuri VALUES ('5', 'IP', 2, 2, 6);
INSERT INTO cursuri VALUES ('6', 'TW', 2, 2, 6);
INSERT INTO cursuri VALUES ('7', 'RN', 3, 1, 4);
INSERT INTO cursuri VALUES ('8', 'Python', 3, 1, 4);
INSERT INTO cursuri VALUES ('9', 'ML', 3, 1, 6);

INSERT INTO profesori VALUES ('1', 'Masalagiu', 'Cristian', 'Prof');
INSERT INTO profesori VALUES ('2', 'Buraga', 'Sabin', 'Conf');
INSERT INTO profesori VALUES ('3', 'Lucanu', 'Dorel', 'Prof');
INSERT INTO profesori VALUES ('4', 'Ciortuz', 'Liviu', 'Conf');
INSERT INTO profesori VALUES ('5', 'Iacob', 'Florin', 'Lect');
INSERT INTO profesori VALUES ('6', 'Breaban', 'Mihaela', 'Conf');
INSERT INTO profesori VALUES ('7', 'Varlan', 'Cosmin', 'Lect');
INSERT INTO profesori VALUES ('8', 'Frasinaru', 'Cristian', 'Prof');
INSERT INTO profesori VALUES ('9', 'Ciobaca', 'Stefan', 'Conf');
INSERT INTO profesori VALUES ('10', 'Gavrilut', 'Dragos', 'Conf');

INSERT INTO profesori VALUES('11', 'Pascariu', 'Georgiana', null);
INSERT INTO profesori VALUES('12', 'Lazar', 'Lucian', null);
INSERT INTO profesori VALUES('13', 'Kristo', 'Robert', null);
INSERT INTO profesori VALUES('14', 'Nastasa', 'Laura', null);
INSERT INTO profesori VALUES('15', 'PASAT', 'Tiberiu', null);

INSERT INTO didactic VALUES ('1','1');
INSERT INTO didactic VALUES ('9','1');
INSERT INTO didactic VALUES ('5','2');
INSERT INTO didactic VALUES ('3','3');
INSERT INTO didactic VALUES ('6','4');
INSERT INTO didactic VALUES ('7','4');
INSERT INTO didactic VALUES ('8','5');
INSERT INTO didactic VALUES ('2','6');
INSERT INTO didactic VALUES ('10','8');
INSERT INTO didactic VALUES ('4', '9');

INSERT INTO note VALUES ('1', '1', 8, TO_DATE('17/02/2014', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('1', '2', 9, TO_DATE('19/02/2014', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('1', '3', 10, TO_DATE('24/06/2014', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('1', '4', 9, TO_DATE('17/02/2015', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('1', '5', 7, TO_DATE('20/06/2015', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('1', '6', 8, TO_DATE('21/06/2015', 'dd/mm/yyyy'));

INSERT INTO note VALUES ('2', '1', 7, TO_DATE('25/02/2014', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('2', '2', 6, TO_DATE('19/02/2014', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('2', '3', 5, TO_DATE('24/06/2014', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('2', '4', 6, TO_DATE('17/02/2015', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('2', '5', 7, TO_DATE('20/06/2015', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('2', '6', 4, TO_DATE('21/06/2015', 'dd/mm/yyyy'));

INSERT INTO note VALUES ('3', '1', 9, TO_DATE('17/02/2014', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('3', '2', 9, TO_DATE('19/02/2014', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('3', '3', 7, TO_DATE('24/06/2014', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('3', '4', 10, TO_DATE('17/02/2015', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('3', '5', 4, TO_DATE('20/06/2015', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('3', '6', 7, TO_DATE('21/06/2015', 'dd/mm/yyyy'));

INSERT INTO note VALUES ('4', '1', 6, TO_DATE('17/02/2014', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('4', '2', 9, TO_DATE('19/02/2014', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('4', '3', 10, TO_DATE('24/06/2014', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('4', '4', 4, TO_DATE('17/02/2015', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('4', '5', 5, TO_DATE('20/06/2015', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('4', '6', 4, TO_DATE('21/06/2015', 'dd/mm/yyyy'));

INSERT INTO note VALUES ('5', '1', 10, TO_DATE('17/02/2014', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('5', '2', 7, TO_DATE('19/02/2014', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('5', '3', 10, TO_DATE('24/06/2014', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('5', '4', 10, TO_DATE('17/02/2015', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('5', '5', 8, TO_DATE('20/06/2015', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('5', '6', 9, TO_DATE('21/06/2015', 'dd/mm/yyyy'));

INSERT INTO note VALUES ('6', '1', 8, TO_DATE('22/02/2015', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('6', '2', 7, TO_DATE('24/02/2015', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('6', '3', 9, TO_DATE('25/06/2015', 'dd/mm/yyyy'));

INSERT INTO note VALUES ('7', '1', 10, TO_DATE('18/02/2015', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('7', '2', 10, TO_DATE('20/02/2015', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('7', '3', 9, TO_DATE('21/06/2015', 'dd/mm/yyyy'));

INSERT INTO note VALUES ('8', '1', 7, TO_DATE('18/02/2015', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('8', '2', 6, TO_DATE('20/02/2015', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('8', '3', 4, TO_DATE('25/06/2015', 'dd/mm/yyyy'));

INSERT INTO note VALUES ('9', '1', 7, TO_DATE('22/02/2015', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('9', '2', 7, TO_DATE('24/02/2015', 'dd/mm/yyyy'));
INSERT INTO note VALUES ('9', '3', 7, TO_DATE('21/06/2015', 'dd/mm/yyyy'));