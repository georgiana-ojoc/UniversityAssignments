SET SERVEROUTPUT ON;

BEGIN
    drop_if_exists('raspunsuri', 'table');
    drop_if_exists('intrebari', 'table');
    drop_if_exists('domenii', 'table');
END;
/
CREATE TABLE domenii
(
    id       NUMBER      NOT NULL PRIMARY KEY,
    denumire VARCHAR(64) NOT NULL
);
CREATE TABLE intrebari
(
    id_domeniu NUMBER        NOT NULL,
    id         NUMBER        NOT NULL PRIMARY KEY,
    continut   VARCHAR2(256) NOT NULL,
    CONSTRAINT fk_domenii
        FOREIGN KEY (id_domeniu)
            REFERENCES domenii (id)
);
CREATE TABLE raspunsuri
(
    id_intrebare NUMBER        NOT NULL,
    id           NUMBER        NOT NULL PRIMARY KEY,
    continut     VARCHAR2(256) NOT NULL,
    corect       NUMBER(1)     NOT NULL,
    CONSTRAINT fk_intrebari
        FOREIGN KEY (id_intrebare)
            REFERENCES intrebari (id)
);
/
INSERT INTO domenii
VALUES (1, 'geometrie');
INSERT INTO intrebari
VALUES (1, 1, 'Care dintre urmatoarele figuri geometrice au patru laturi?');
INSERT INTO raspunsuri
VALUES (1, 1, 'Patrat', 1);
INSERT INTO raspunsuri
VALUES (1, 2, 'Cerc', 0);
INSERT INTO raspunsuri
VALUES (1, 3, 'Triunghi', 0);
INSERT INTO raspunsuri
VALUES (1, 4, 'Romb', 1);
INSERT INTO raspunsuri
VALUES (1, 5, 'Dreptunghi', 1);
INSERT INTO raspunsuri
VALUES (1, 6, 'Paralelogram', 1);
INSERT INTO raspunsuri
VALUES (1, 7, 'Hexagon', 0);
INSERT INTO raspunsuri
VALUES (1, 8, 'Dodecagon', 0);
INSERT INTO raspunsuri
VALUES (1, 9, 'Pentagon', 0);
INSERT INTO raspunsuri
VALUES (1, 10, 'Poligon', 0);
/
INSERT INTO intrebari
VALUES (1, 2, 'Care dintre urmatoarele figuri geometrice au mai mult de patru colturi?');
INSERT INTO raspunsuri
VALUES (2, 11, 'Patrat', 0);
INSERT INTO raspunsuri
VALUES (2, 12, 'Cerc', 1);
INSERT INTO raspunsuri
VALUES (2, 13, 'Triunghi', 0);
INSERT INTO raspunsuri
VALUES (2, 14, 'Romb', 0);
INSERT INTO raspunsuri
VALUES (2, 15, 'Dreptunghi', 0);
INSERT INTO raspunsuri
VALUES (2, 16, 'Paralelogram', 0);
INSERT INTO raspunsuri
VALUES (2, 17, 'Hexagon', 1);
INSERT INTO raspunsuri
VALUES (2, 18, 'Dodecagon', 1);
INSERT INTO raspunsuri
VALUES (2, 19, 'Pentagon', 1);
INSERT INTO raspunsuri
VALUES (2, 20, 'Poligon', 0);
/
INSERT INTO intrebari
VALUES (1, 3, 'Care dintre urmatoarele afirmatii sunt adevarate?');
INSERT INTO raspunsuri
VALUES (3, 21, 'Triunghiul are patru laturi.', 0);
INSERT INTO raspunsuri
VALUES (3, 22, 'Cercul are o infinitate de varfuri.', 1);
INSERT INTO raspunsuri
VALUES (3, 23, 'Suma unghiurilor unui triunghi este de 180 de grade.', 1);
INSERT INTO raspunsuri
VALUES (3, 24, 'Suma unghiurilor unui patrat este de 180 de grade.', 0);
INSERT INTO raspunsuri
VALUES (3, 25, 'Un patrat este un romb cu un unghi drept.', 1);
INSERT INTO raspunsuri
VALUES (3, 26, 'Un poligon este o linie franta inchisa.', 1);
INSERT INTO raspunsuri
VALUES (3, 27, 'Intr-un triunghi dreptunghic suma catetelor este egala cu ipotenuza.', 0);
/
INSERT INTO intrebari
VALUES (1, 4, 'Care dintre urmatoarele figuri geometrice au toate laturile egale?');
INSERT INTO raspunsuri
VALUES (4, 28, 'Triunghiul dreptunghic', 0);
INSERT INTO raspunsuri
VALUES (4, 29, 'Triunghiul isoscel', 0);
INSERT INTO raspunsuri
VALUES (4, 30, 'Triunghiul echilateral', 1);
INSERT INTO raspunsuri
VALUES (4, 31, 'Patratul', 1);
INSERT INTO raspunsuri
VALUES (4, 32, 'Rombul', 1);
INSERT INTO raspunsuri
VALUES (4, 33, 'Dreptunghiul', 0);
INSERT INTO raspunsuri
VALUES (4, 34, 'Hexagonul neregulat', 0);
/
INSERT INTO domenii
VALUES (2, 'numere');
INSERT INTO intrebari
VALUES (2, 5, 'Care dintre urmatoarele numere sunt prime?');
INSERT INTO raspunsuri
VALUES (5, 35, '3', 1);
INSERT INTO raspunsuri
VALUES (5, 36, '7', 1);
INSERT INTO raspunsuri
VALUES (5, 37, '5', 1);
INSERT INTO raspunsuri
VALUES (5, 38, '13', 1);
INSERT INTO raspunsuri
VALUES (5, 39, '17', 1);
INSERT INTO raspunsuri
VALUES (5, 40, '9', 0);
INSERT INTO raspunsuri
VALUES (5, 41, '16', 0);
INSERT INTO raspunsuri
VALUES (5, 42, '22', 0);
INSERT INTO raspunsuri
VALUES (5, 43, '121', 0);
INSERT INTO raspunsuri
VALUES (5, 44, '14', 0);
/
INSERT INTO intrebari
VALUES (2, 6, 'Care dintre urmatoarele numere sunt pare?');
INSERT INTO raspunsuri
VALUES (6, 45, '2', 1);
INSERT INTO raspunsuri
VALUES (6, 46, '4', 1);
INSERT INTO raspunsuri
VALUES (6, 47, '6', 1);
INSERT INTO raspunsuri
VALUES (6, 48, '12', 1);
INSERT INTO raspunsuri
VALUES (6, 49, '100', 1);
INSERT INTO raspunsuri
VALUES (6, 50, '13', 0);
INSERT INTO raspunsuri
VALUES (6, 51, '15', 0);
INSERT INTO raspunsuri
VALUES (6, 52, '1', 0);
INSERT INTO raspunsuri
VALUES (6, 53, '7', 0);
INSERT INTO raspunsuri
VALUES (6, 54, '9', 0);
/
INSERT INTO intrebari
VALUES (2, 7, 'Care dintre urmatoarele numere sunt din sirul lui Fibonacci?');
INSERT INTO raspunsuri
VALUES (7, 55, '1', 1);
INSERT INTO raspunsuri
VALUES (7, 56, '2', 1);
INSERT INTO raspunsuri
VALUES (7, 57, '3', 1);
INSERT INTO raspunsuri
VALUES (7, 58, '5', 1);
INSERT INTO raspunsuri
VALUES (7, 59, '8', 1);
INSERT INTO raspunsuri
VALUES (7, 60, '13', 1);
INSERT INTO raspunsuri
VALUES (7, 61, '21', 1);
INSERT INTO raspunsuri
VALUES (7, 62, '22', 0);
INSERT INTO raspunsuri
VALUES (7, 63, '23', 0);
/
INSERT INTO domenii
VALUES (3, 'flori');
INSERT INTO intrebari
VALUES (3, 8, 'Care flori sunt sau pot fi albe?');
INSERT INTO raspunsuri
VALUES (8, 64, 'Ghiocelul', 1);
INSERT INTO raspunsuri
VALUES (8, 65, 'Margareta', 1);
INSERT INTO raspunsuri
VALUES (8, 66, 'Trandafirul', 1);
INSERT INTO raspunsuri
VALUES (8, 67, 'Floarea de soc', 1);
INSERT INTO raspunsuri
VALUES (8, 68, 'Papadia', 0);
INSERT INTO raspunsuri
VALUES (8, 69, 'Floarea de cires', 1);
INSERT INTO raspunsuri
VALUES (8, 70, 'Crinul', 1);
INSERT INTO raspunsuri
VALUES (8, 71, 'Bumbisorul', 1);
INSERT INTO raspunsuri
VALUES (8, 72, 'Floarea soarelui', 0);
/
INSERT INTO intrebari
VALUES (3, 9, 'Care dintre urmatorele nume de fete sunt si nume de flori?');
INSERT INTO raspunsuri
VALUES (9, 73, 'Crina', 1);
INSERT INTO raspunsuri
VALUES (9, 74, 'Margareta', 1);
INSERT INTO raspunsuri
VALUES (9, 75, 'Lacramioara', 1);
INSERT INTO raspunsuri
VALUES (9, 76, 'Madalina', 0);
INSERT INTO raspunsuri
VALUES (9, 77, 'Maria', 0);
INSERT INTO raspunsuri
VALUES (9, 78, 'Larisa', 0);
INSERT INTO raspunsuri
VALUES (9, 79, 'Georgiana', 0);
INSERT INTO raspunsuri
VALUES (9, 80, 'Brandusa', 1);
INSERT INTO raspunsuri
VALUES (9, 81, 'Ana', 0);
/
INSERT INTO domenii
VALUES (4, 'alfabet');
INSERT INTO intrebari
VALUES (4, 10, 'Cate litere are alfabetul roman?');
INSERT INTO raspunsuri
VALUES (10, 82, '26', 0);
INSERT INTO raspunsuri
VALUES (10, 83, '31', 1);
INSERT INTO raspunsuri
VALUES (10, 84, 'Ce e asta?', 0);
INSERT INTO raspunsuri
VALUES (10, 85, '26.5', 0);
INSERT INTO raspunsuri
VALUES (10, 86, '-12', 0);
INSERT INTO raspunsuri
VALUES (10, 87, 'Pe toate', 0);
/
INSERT INTO domenii
VALUES (5, 'existentialism');
INSERT INTO intrebari
VALUES (5, 11, 'Pe ce planeta traiesti?');
INSERT INTO raspunsuri
VALUES (11, 88, 'Pe Pamant', 1);
INSERT INTO raspunsuri
VALUES (11, 89, 'In nori.', 0);
INSERT INTO raspunsuri
VALUES (11, 90, 'Pe Jupiter', 0);
INSERT INTO raspunsuri
VALUES (11, 91, 'Depinde.', 0);
INSERT INTO raspunsuri
VALUES (11, 92, 'Pe Luna', 0);
INSERT INTO raspunsuri
VALUES (11, 93, 'Pe toate', 0);
/
INSERT INTO domenii
VALUES (6, 'animale');
INSERT INTO intrebari
VALUES (6, 12, 'Care animal este mamifer?');
INSERT INTO raspunsuri
VALUES (12, 94, 'Ariciul', 1);
INSERT INTO raspunsuri
VALUES (12, 95, 'Capra neagra', 1);
INSERT INTO raspunsuri
VALUES (12, 96, 'Lupul', 1);
INSERT INTO raspunsuri
VALUES (12, 97, 'Ursul', 1);
INSERT INTO raspunsuri
VALUES (12, 98, 'Gaina', 0);
INSERT INTO raspunsuri
VALUES (12, 99, 'Barza', 0);
INSERT INTO raspunsuri
VALUES (12, 100, 'Sarpele', 0);
INSERT INTO raspunsuri
VALUES (12, 101, 'Delfinul', 1);
INSERT INTO raspunsuri
VALUES (12, 102, 'Broasca testoasa', 0);
/
INSERT INTO domenii
VALUES (7, 'povesti');
INSERT INTO intrebari
VALUES (7, 13, 'Cati pitici avea Cenusareasa?');
INSERT INTO raspunsuri
VALUES (13, 103, '0', 1);
INSERT INTO raspunsuri
VALUES (13, 104, 'Niciunul', 1);
INSERT INTO raspunsuri
VALUES (13, 105, '1', 0);
INSERT INTO raspunsuri
VALUES (13, 106, '3', 0);
INSERT INTO raspunsuri
VALUES (13, 107, '7', 0);
INSERT INTO raspunsuri
VALUES (13, 108, '8', 0);
INSERT INTO raspunsuri
VALUES (13, 109, 'Pe toti', 0);
INSERT INTO raspunsuri
VALUES (13, 110, 'Unul si bun', 0);
INSERT INTO raspunsuri
VALUES (13, 111, 'Nu stiu', 0);
/
INSERT INTO domenii
VALUES (8, 'istorie');
INSERT INTO intrebari
VALUES (8, 14, 'Cine a fost Alexandru Ioan Cuza?');
INSERT INTO raspunsuri
VALUES (14, 112, 'A fost primul domnitor al Principatelor Unite si al statului national Romania.', 1);
INSERT INTO raspunsuri
VALUES (14, 113, 'A fost un om inrobit de doua patimi: iubirea pentru patrie si iubirea pentru femei frumoase.', 1);
INSERT INTO raspunsuri
VALUES (14, 114, 'A fost un om pasionat de cai.', 1);
INSERT INTO raspunsuri
VALUES (14, 115, 'A fost un pictor roman.', 0);
INSERT INTO raspunsuri
VALUES (14, 116, 'A fost primul scriitor de opera literara romana.', 0);
INSERT INTO raspunsuri
VALUES (14, 117, 'A fost ultimul domnitor al Principatelor Unite si al statului national Romania.', 0);
INSERT INTO raspunsuri
VALUES (14, 118, 'A fost un domnitor roman nascut in anul 1820.', 1);
INSERT INTO raspunsuri
VALUES (14, 119, 'A fost un domnitor roman nascut in anul 1859.', 0);
INSERT INTO raspunsuri
VALUES (14, 120, 'A fost un domnitor intre anii 1859-1866.', 1);
/
INSERT INTO domenii
VALUES (9, 'scriitori');
INSERT INTO intrebari
VALUES (9, 15, 'Care sunt scriitori romani?');
INSERT INTO raspunsuri
VALUES (15, 121, 'Mihai Eminescu', 1);
INSERT INTO raspunsuri
VALUES (15, 122, 'Ion Luca Caragiale', 1);
INSERT INTO raspunsuri
VALUES (15, 123, 'Mircea Cartarescu', 1);
INSERT INTO raspunsuri
VALUES (15, 124, 'Mircea Eliade', 1);
INSERT INTO raspunsuri
VALUES (15, 125, 'Ion Creanga', 1);
INSERT INTO raspunsuri
VALUES (15, 126, 'Liviu Rebreanu', 1);
INSERT INTO raspunsuri
VALUES (15, 127, 'Nicolae Grigorescu', 0);
INSERT INTO raspunsuri
VALUES (15, 128, 'Nicolae Tonitza', 0);
INSERT INTO raspunsuri
VALUES (15, 129, 'Stefan Luchian', 0);
INSERT INTO raspunsuri
VALUES (15, 130, 'Ion Andreescu', 0);
/
INSERT INTO domenii
VALUES (10, 'filme');
INSERT INTO intrebari
VALUES (10, 16, 'Care dintre urmatoarele filme il au drept regizor pe Quentin Tarantino?');
INSERT INTO raspunsuri
VALUES (16, 131, 'From Dusk till Dawn', 1);
INSERT INTO raspunsuri
VALUES (16, 132, 'Pulp Fiction', 1);
INSERT INTO raspunsuri
VALUES (16, 133, 'Django', 1);
INSERT INTO raspunsuri
VALUES (16, 134, 'Eyes Wide Shut', 0);
INSERT INTO raspunsuri
VALUES (16, 135, 'Terminator', 0);
INSERT INTO raspunsuri
VALUES (16, 136, 'The Godfather', 0);
INSERT INTO raspunsuri
VALUES (16, 137, 'Kill Bill', 1);
INSERT INTO raspunsuri
VALUES (16, 138, 'Ender’s Game', 0);
INSERT INTO raspunsuri
VALUES (16, 139, 'Coboram la prima', 0);
INSERT INTO raspunsuri
VALUES (16, 140, 'The Notebook', 0);
/
INSERT INTO domenii
VALUES (11, 'geografie');
INSERT INTO intrebari
VALUES (11, 17, 'Cu ce tari se invecineaza Romania?');
INSERT INTO raspunsuri
VALUES (17, 141, 'Ucraina', 1);
INSERT INTO raspunsuri
VALUES (17, 142, 'Rusia', 0);
INSERT INTO raspunsuri
VALUES (17, 143, 'Austria', 0);
INSERT INTO raspunsuri
VALUES (17, 144, 'Ungaria', 1);
INSERT INTO raspunsuri
VALUES (17, 145, 'Serbia', 1);
INSERT INTO raspunsuri
VALUES (17, 146, 'Germania', 0);
INSERT INTO raspunsuri
VALUES (17, 147, 'Bulgaria', 1);
INSERT INTO raspunsuri
VALUES (17, 148, 'Republica Moldova', 1);
INSERT INTO raspunsuri
VALUES (17, 149, 'Spania', 0);
INSERT INTO raspunsuri
VALUES (17, 150, 'Macedonia', 0);
/
INSERT INTO intrebari
VALUES (5, 18, 'Care este raspunsul la orice intrebare de tip "Da sau nu?"?');
INSERT INTO raspunsuri
VALUES (18, 151, 'Tu ce crezi?', 1);
INSERT INTO raspunsuri
VALUES (18, 152, 'Probabil.', 1);
INSERT INTO raspunsuri
VALUES (18, 153, 'Nu stiu.', 0);
INSERT INTO raspunsuri
VALUES (18, 154, 'Da.', 0);
INSERT INTO raspunsuri
VALUES (18, 155, 'Nu.', 0);
INSERT INTO raspunsuri
VALUES (18, 156, 'Depinde.', 0);
/
INSERT INTO intrebari
VALUES (7, 19, 'Cine a scris "Capra cu trei iezi"?');
INSERT INTO raspunsuri
VALUES (19, 157, 'Killa Fonic', 0);
INSERT INTO raspunsuri
VALUES (19, 158, 'Ion Creanga', 1);
INSERT INTO raspunsuri
VALUES (19, 159, 'Este povestire populara.', 0);
INSERT INTO raspunsuri
VALUES (19, 160, 'Vasile Alecsandri', 0);
INSERT INTO raspunsuri
VALUES (19, 161, 'Cristian Tudor Popescu', 0);
INSERT INTO raspunsuri
VALUES (19, 162, 'Mihail Sadoveanu', 0);
/
INSERT INTO intrebari
VALUES (11, 20, 'Care tara este si continent?');
INSERT INTO raspunsuri
VALUES (20, 163, 'Romania', 0);
INSERT INTO raspunsuri
VALUES (20, 164, 'Australia', 1);
INSERT INTO raspunsuri
VALUES (20, 165, 'Statele Unite ale Americii', 0);
INSERT INTO raspunsuri
VALUES (20, 166, 'China', 0);
INSERT INTO raspunsuri
VALUES (20, 167, 'Africa de Sud', 0);
INSERT INTO raspunsuri
VALUES (20, 168, 'Rusia', 0);
/
INSERT INTO intrebari
VALUES (9, 21, 'Care dintre urmatoarele opere au fost scrise de Mircea Eliade?');
INSERT INTO raspunsuri
VALUES (21, 169, 'Maitreyi', 1);
INSERT INTO raspunsuri
VALUES (21, 170, 'Romanul adolescentului miop', 1);
INSERT INTO raspunsuri
VALUES (21, 171, 'Noaptea de Sanziene', 1);
INSERT INTO raspunsuri
VALUES (21, 172, 'Domnisoara Christina', 1);
INSERT INTO raspunsuri
VALUES (21, 173, 'La tiganci', 1);
INSERT INTO raspunsuri
VALUES (21, 174, 'Pe culmile disperarii', 0);
INSERT INTO raspunsuri
VALUES (21, 175, 'Demiurgul cel rau', 0);
INSERT INTO raspunsuri
VALUES (21, 176, 'Despre neajunsul de a te fi nascut', 0);
/
INSERT INTO domenii
VALUES (12, 'musica');
INSERT INTO intrebari
VALUES (12, 22, 'Care dintre urmatoarele note muzicale sunt valide?');
INSERT INTO raspunsuri
VALUES (22, 177, 'Do', 1);
INSERT INTO raspunsuri
VALUES (22, 178, 'Re', 1);
INSERT INTO raspunsuri
VALUES (22, 179, 'Mi', 1);
INSERT INTO raspunsuri
VALUES (22, 180, 'Fa', 1);
INSERT INTO raspunsuri
VALUES (22, 181, 'Sol', 1);
INSERT INTO raspunsuri
VALUES (22, 182, 'La', 1);
INSERT INTO raspunsuri
VALUES (22, 183, 'Si', 1);
INSERT INTO raspunsuri
VALUES (22, 184, 'Ri', 0);
INSERT INTO raspunsuri
VALUES (22, 185, 'Mu', 0);
INSERT INTO raspunsuri
VALUES (22, 186, 'Lo', 0);
/
INSERT INTO intrebari
VALUES (12, 24, 'Care dintre urmatorii artisti sunt francezi?');
INSERT INTO raspunsuri
VALUES (24, 197, 'Stromae', 1);
INSERT INTO raspunsuri
VALUES (24, 198, 'Indila', 1);
INSERT INTO raspunsuri
VALUES (24, 199, 'Pomme', 1);
INSERT INTO raspunsuri
VALUES (24, 200, 'Black M', 1);
INSERT INTO raspunsuri
VALUES (24, 201, 'Eminem', 0);
INSERT INTO raspunsuri
VALUES (24, 202, 'Illenium', 0);
INSERT INTO raspunsuri
VALUES (24, 203, 'Billie Eilish', 0);
INSERT INTO raspunsuri
VALUES (24, 204, 'Martin Garrix', 0);
INSERT INTO raspunsuri
VALUES (24, 205, 'Alan Walker', 0);
INSERT INTO raspunsuri
VALUES (24, 206, 'Faruko', 0);
/
INSERT INTO intrebari
VALUES (9, 23, 'Care dintre urmatoarele poezii sunt scrise de catre Mihai Eminescu?');
INSERT INTO raspunsuri
VALUES (23, 187, 'Floare albastra', 1);
INSERT INTO raspunsuri
VALUES (23, 188, 'Luceafarul', 1);
INSERT INTO raspunsuri
VALUES (23, 189, 'Iarna pe ulita', 0);
INSERT INTO raspunsuri
VALUES (23, 190, 'Somnoroase pasarele', 1);
INSERT INTO raspunsuri
VALUES (23, 191, 'Plumb', 0);
INSERT INTO raspunsuri
VALUES (23, 192, 'Si daca...', 1);
INSERT INTO raspunsuri
VALUES (23, 193, 'Testament', 0);
INSERT INTO raspunsuri
VALUES (23, 194, 'Acceleratul', 0);
INSERT INTO raspunsuri
VALUES (23, 195, 'De pe-o buna dimineata', 0);
INSERT INTO raspunsuri
VALUES (23, 196, 'Rapsodii de toamna', 0);
/
INSERT INTO domenii
VALUES (13, 'TIC');
INSERT INTO intrebari
VALUES (13, 25, 'Care dintre urmatoarele dispozitive periferice sunt de iesire?');
INSERT INTO raspunsuri
VALUES (25, 207, 'Tastatura', 0);
INSERT INTO raspunsuri
VALUES (25, 208, 'Imprimanta', 1);
INSERT INTO raspunsuri
VALUES (25, 209, 'Difuzor', 1);
INSERT INTO raspunsuri
VALUES (25, 210, 'Mouse', 0);
INSERT INTO raspunsuri
VALUES (25, 211, 'Microfon', 0);
INSERT INTO raspunsuri
VALUES (25, 212, 'Monitor', 1);
INSERT INTO raspunsuri
VALUES (25, 213, 'Camera video', 0);
INSERT INTO raspunsuri
VALUES (25, 214, 'Plotter', 1);
INSERT INTO raspunsuri
VALUES (25, 215, 'Trackball', 0);
INSERT INTO raspunsuri
VALUES (25, 216, 'Scanner', 0);
/
INSERT INTO domenii
VALUES (14, 'informatica');
INSERT INTO intrebari
VALUES (14, 26, 'Care dintre urmatoarele tipuri de sortare au o complexitate medie de O(n * log(n))?');
INSERT INTO raspunsuri
VALUES (26, 217, 'Bubble Sort', 0);
INSERT INTO raspunsuri
VALUES (26, 218, 'Quick Sort', 1);
INSERT INTO raspunsuri
VALUES (26, 219, 'Insertion Sort', 0);
INSERT INTO raspunsuri
VALUES (26, 220, 'Heap Sort', 1);
INSERT INTO raspunsuri
VALUES (26, 221, 'Merge Sort', 1);
INSERT INTO raspunsuri
VALUES (26, 222, 'Bucket Sort', 0);
INSERT INTO raspunsuri
VALUES (26, 223, 'Selection Sort', 0);
/
INSERT INTO intrebari
VALUES (14, 27, 'Care dintre urmatoarele raspunsuri sunt limbaje de programare?');
INSERT INTO raspunsuri
VALUES (27, 224, 'HTML', 0);
INSERT INTO raspunsuri
VALUES (27, 225, 'C', 1);
INSERT INTO raspunsuri
VALUES (27, 226, 'CSS', 0);
INSERT INTO raspunsuri
VALUES (27, 227, 'Java', 1);
INSERT INTO raspunsuri
VALUES (27, 228, 'C#', 1);
INSERT INTO raspunsuri
VALUES (27, 229, 'C++', 1);
INSERT INTO raspunsuri
VALUES (27, 230, 'Python', 1);
INSERT INTO raspunsuri
VALUES (27, 231, 'Semnelor', 0);
INSERT INTO raspunsuri
VALUES (27, 232, 'Autostopul', 0);
/
INSERT INTO intrebari
VALUES (14, 28, 'Care dintre variante reprezinta un cuvant intalnit des in informatica?');
INSERT INTO raspunsuri
VALUES (28, 233, 'Byte', 1);
INSERT INTO raspunsuri
VALUES (28, 234, 'Eroare', 1);
INSERT INTO raspunsuri
VALUES (28, 235, 'Aplicatie', 1);
INSERT INTO raspunsuri
VALUES (28, 236, 'Spatiu', 1);
INSERT INTO raspunsuri
VALUES (28, 237, 'Timp', 1);
INSERT INTO raspunsuri
VALUES (28, 238, 'Volum', 0);
INSERT INTO raspunsuri
VALUES (28, 239, 'Acid Ribonucleic', 0);
INSERT INTO raspunsuri
VALUES (28, 240, 'Restanta', 0);
INSERT INTO raspunsuri
VALUES (28, 241, 'Arsen', 0);
INSERT INTO raspunsuri
VALUES (28, 242, 'Impedanta', 0);
/
INSERT INTO intrebari
VALUES (14, 29, 'Care este maestrul xiaolin?');
INSERT INTO raspunsuri
VALUES (29, 243, 'Omni', 1);
INSERT INTO raspunsuri
VALUES (29, 244, 'Kai', 1);
INSERT INTO raspunsuri
VALUES (29, 245, 'Raimundo', 1);
INSERT INTO raspunsuri
VALUES (29, 246, 'Kimiko', 1);
INSERT INTO raspunsuri
VALUES (29, 247, 'Wuya', 0);
INSERT INTO raspunsuri
VALUES (29, 248, 'Jack', 0);
INSERT INTO raspunsuri
VALUES (29, 249, 'Chase', 0);
INSERT INTO raspunsuri
VALUES (29, 250, 'Master Fung', 0);
INSERT INTO raspunsuri
VALUES (29, 251, 'Nu stiu.', 0);
INSERT INTO raspunsuri
VALUES (29, 252, 'Bruce Lee', 0);
/
INSERT INTO intrebari
VALUES (10, 30, 'Ce personaje au murit in Avengers: Endgame?');
INSERT INTO raspunsuri
VALUES (30, 301, 'Black Widow', 1);
INSERT INTO raspunsuri
VALUES (30, 302, 'Tony Stark', 1);
INSERT INTO raspunsuri
VALUES (30, 303, 'Hawkeye', 0);
INSERT INTO raspunsuri
VALUES (30, 304, 'Captain America', 0);
INSERT INTO raspunsuri
VALUES (30, 305, 'Scarlet Witch', 0);
INSERT INTO raspunsuri
VALUES (30, 306, 'Roket', 0);
INSERT INTO raspunsuri
VALUES (30, 307, 'Gamora', 0);
INSERT INTO raspunsuri
VALUES (30, 308, 'Spider-Man', 0);
INSERT INTO raspunsuri
VALUES (30, 309, 'Nu stiu.', 0);
INSERT INTO raspunsuri
VALUES (30, 310, 'Thanos', 1);
/
INSERT INTO domenii
VALUES (15, 'astronomie');
INSERT INTO intrebari
VALUES (15, 31, 'Care dintre urmatoarele planete efectueaza o miscare retrograda?');
INSERT INTO raspunsuri
VALUES (31, 311, 'Mercur', 0);
INSERT INTO raspunsuri
VALUES (31, 312, 'Venus', 1);
INSERT INTO raspunsuri
VALUES (31, 313, 'Pamant', 0);
INSERT INTO raspunsuri
VALUES (31, 314, 'Marte', 0);
INSERT INTO raspunsuri
VALUES (31, 315, 'Jupiter', 0);
INSERT INTO raspunsuri
VALUES (31, 316, 'Saturn', 0);
INSERT INTO raspunsuri
VALUES (31, 317, 'Uranus', 1);
INSERT INTO raspunsuri
VALUES (31, 318, 'Neptun', 0);
INSERT INTO raspunsuri
VALUES (31, 319, 'Pluto', 1);
/
INSERT INTO intrebari
VALUES (14, 32, 'Care dintre urmatoarele sisteme de baze de date sunt relationale?');
INSERT INTO raspunsuri
VALUES (32, 320, 'PostgreSQL', 1);
INSERT INTO raspunsuri
VALUES (32, 321, 'Microsoft SQL Server', 1);
INSERT INTO raspunsuri
VALUES (32, 322, 'Microsoft Azure SQL', 1);
INSERT INTO raspunsuri
VALUES (32, 323, 'Apache CouchDB', 0);
INSERT INTO raspunsuri
VALUES (32, 324, 'Redis', 0);
INSERT INTO raspunsuri
VALUES (32, 325, 'MongoDB', 0);
INSERT INTO raspunsuri
VALUES (32, 326, 'SQLite', 1);
INSERT INTO raspunsuri
VALUES (32, 327, 'Cassandra', 0);
INSERT INTO raspunsuri
VALUES (32, 328, 'HBase', 0);
INSERT INTO raspunsuri
VALUES (32, 329, 'MariaDB', 1);
/
INSERT INTO intrebari
VALUES (14, 33, 'Care dintre urmatoarele variante sunt sisteme de operare?');
INSERT INTO raspunsuri
VALUES (33, 330, 'Windows', 1);
INSERT INTO raspunsuri
VALUES (33, 331, 'MD-DOS', 1);
INSERT INTO raspunsuri
VALUES (33, 332, 'Ubuntu', 1);
INSERT INTO raspunsuri
VALUES (33, 333, 'HTML', 0);
INSERT INTO raspunsuri
VALUES (33, 334, 'USB', 0);
INSERT INTO raspunsuri
VALUES (33, 335, 'JVM', 0);
INSERT INTO raspunsuri
VALUES (33, 336, 'TempleOS', 1);
INSERT INTO raspunsuri
VALUES (33, 337, 'BananaOS', 0);
INSERT INTO raspunsuri
VALUES (33, 338, 'Antena 5G', 0);
INSERT INTO raspunsuri
VALUES (33, 339, 'MacOS', 1);
/
INSERT INTO intrebari
VALUES (14, 34, 'Cine a inventat Git?');
INSERT INTO raspunsuri
VALUES (34, 340, 'Linus Torvalds', 1);
INSERT INTO raspunsuri
VALUES (34, 341, 'Bill Gates', 0);
INSERT INTO raspunsuri
VALUES (34, 342, 'Dennis Ritchie', 0);
INSERT INTO raspunsuri
VALUES (34, 343, 'RMS', 0);
INSERT INTO raspunsuri
VALUES (34, 344, 'Microsoft', 0);
INSERT INTO raspunsuri
VALUES (34, 345, 'Github', 0);
/
INSERT INTO intrebari
VALUES (12, 35, 'Care dintre urmatoarele variante sunt titluri de albume solo ale lui Michael Jackson?');
INSERT INTO raspunsuri
VALUES (35, 346, 'Bad', 1);
INSERT INTO raspunsuri
VALUES (35, 347, 'Good', 0);
INSERT INTO raspunsuri
VALUES (35, 348, 'Ben', 1);
INSERT INTO raspunsuri
VALUES (35, 349, 'Innervisions', 0);
INSERT INTO raspunsuri
VALUES (35, 350, 'The White Album', 0);
INSERT INTO raspunsuri
VALUES (35, 351, 'Dangerous', 1);
INSERT INTO raspunsuri
VALUES (35, 352, 'Life After Death', 0);
INSERT INTO raspunsuri
VALUES (35, 353, 'Invincible', 1);
INSERT INTO raspunsuri
VALUES (35, 354, 'Victory', 0);
INSERT INTO raspunsuri
VALUES (35, 355, 'My Life', 0);
/
INSERT INTO intrebari
VALUES (14, 36, 'Care dintre urmatoarele variante NU sunt distributii de Linux?');
INSERT INTO raspunsuri
VALUES (36, 356, 'Arch Linux', 0);
INSERT INTO raspunsuri
VALUES (36, 357, 'Void Linux', 0);
INSERT INTO raspunsuri
VALUES (36, 358, 'Linux Cinnamon', 1);
INSERT INTO raspunsuri
VALUES (36, 359, 'Linux Mint', 0);
INSERT INTO raspunsuri
VALUES (36, 360, 'PopOS', 0);
INSERT INTO raspunsuri
VALUES (36, 361, 'Red Hat Linux', 0);
INSERT INTO raspunsuri
VALUES (36, 362, 'Black Hat Linux', 1);
INSERT INTO raspunsuri
VALUES (36, 363, 'White Hat Linux', 1);
INSERT INTO raspunsuri
VALUES (36, 364, 'Ubuntu', 0);
INSERT INTO raspunsuri
VALUES (36, 365, 'Kubuntu', 0);
INSERT INTO raspunsuri
VALUES (36, 366, 'Lubuntu', 0);
INSERT INTO raspunsuri
VALUES (36, 367, 'Xubuntu', 0);
INSERT INTO raspunsuri
VALUES (36, 368, 'Nubuntu', 1);
/
INSERT INTO domenii
VALUES (16, 'biologie');
INSERT INTO intrebari
VALUES (16, 37, 'Ce organit celular este supranumit "the powerhouse of the cell"?');
INSERT INTO raspunsuri
VALUES (37, 369, 'Mitocondria', 1);
INSERT INTO raspunsuri
VALUES (37, 370, 'Mitocendria', 0);
INSERT INTO raspunsuri
VALUES (37, 371, 'Mitucondria', 0);
INSERT INTO raspunsuri
VALUES (37, 372, 'Mitocondriul', 0);
INSERT INTO raspunsuri
VALUES (37, 373, 'Nucleul', 0);
INSERT INTO raspunsuri
VALUES (37, 374, 'Ribozomul', 0);
INSERT INTO raspunsuri
VALUES (37, 375, 'Aparatul Golgi', 0);
INSERT INTO raspunsuri
VALUES (37, 376, 'Mitocondria, din nou', 1);
/
INSERT INTO intrebari
VALUES (14, 38, 'Care dintre urmatoarele secvente de cod descrie starea mintala a unui student la informatica?');
INSERT INTO raspunsuri
VALUES (38, 377, 'for ( ; _ ; ) { cry(); }', 1);
INSERT INTO raspunsuri
VALUES (38, 378, ': () { :|: & } ; :', 1);
INSERT INTO raspunsuri
VALUES (38, 379, 'ヾ(*’Ｏ’*)/', 0);
INSERT INTO raspunsuri
VALUES (38, 380, '}:-)', 0);
INSERT INTO raspunsuri
VALUES (38, 381, '>.<', 0);
INSERT INTO raspunsuri
VALUES (38, 382, '♪┌|∵|┘♪ └|∵|┐♪', 0);
INSERT INTO raspunsuri
VALUES (38, 383, '⊂(´･◡･⊂ )∘˚˳°', 0);
/
INSERT INTO intrebari
VALUES (14, 39, 'Cate puncte aveti la PSGBD pana acum?');
INSERT INTO raspunsuri
VALUES (39, 384, 'Nu stim.', 1);
INSERT INTO raspunsuri
VALUES (39, 385, '0-10', 0);
INSERT INTO raspunsuri
VALUES (39, 386, '11-20', 0);
INSERT INTO raspunsuri
VALUES (39, 387, '21-30', 0);
INSERT INTO raspunsuri
VALUES (39, 388, '31-40', 0);
INSERT INTO raspunsuri
VALUES (39, 389, '41-50', 0);
INSERT INTO raspunsuri
VALUES (39, 390, '50+', 0);
/
INSERT INTO domenii
VALUES (17, 'culori');
INSERT INTO intrebari
VALUES (17, 40, 'Care dintre urmatoarele culori nu sunt primare?');
INSERT INTO raspunsuri
VALUES (40, 391, 'Verde', 1);
INSERT INTO raspunsuri
VALUES (40, 392, 'Rosu', 0);
INSERT INTO raspunsuri
VALUES (40, 393, 'Gri', 0);
INSERT INTO raspunsuri
VALUES (40, 394, 'Indigo', 1);
INSERT INTO raspunsuri
VALUES (40, 395, 'Albastru', 0);
INSERT INTO raspunsuri
VALUES (40, 396, 'Oranj', 1);
INSERT INTO raspunsuri
VALUES (40, 397, 'Galben', 0);
/
INSERT INTO intrebari
VALUES (17, 41, 'Care dintre urmatoarele culori sunt nuante de roz?');
INSERT INTO raspunsuri
VALUES (41, 398, 'MediumVioletRed', 1);
INSERT INTO raspunsuri
VALUES (41, 399, 'RosyBrown', 0);
INSERT INTO raspunsuri
VALUES (41, 400, 'Fuchsia', 0);
INSERT INTO raspunsuri
VALUES (41, 401, 'HotPink', 1);
INSERT INTO raspunsuri
VALUES (41, 402, 'Thistle', 0);
INSERT INTO raspunsuri
VALUES (41, 403, 'PaleVioletRed', 1);
INSERT INTO raspunsuri
VALUES (41, 404, 'LightCoral ', 0);
/
INSERT INTO domenii
VALUES (18, 'web');
INSERT INTO intrebari
VALUES (18, 42, 'Ce este CORS si ce efect are o exceptie de acest tip asupra unui request?');
INSERT INTO raspunsuri
VALUES (42, 405, 'Este CROSS ORIGIN ACCESS si previne un client din a face request catre un server pe un port diferit.',
        1);
INSERT INTO raspunsuri
VALUES (42, 406, 'Este CROISSANT OREO SECURIZAT si este un warning care spune ca requestul nu are sens.', 0);
INSERT INTO raspunsuri
VALUES (42, 407, 'Este CORD ORIGIN ACCESS si este o exceptie care previne atacatorii din a fura date.', 0);
INSERT INTO raspunsuri
VALUES (42, 408, 'Este CROSS ORIGIN ACCESS si spune browser-elor sa faca request catre aceeasi origine.', 1);
INSERT INTO raspunsuri
VALUES (42, 409, 'Este CROSS ORIGIN ACCESS si previne aplicatiile externe din a folosit un request.', 1);
INSERT INTO raspunsuri
VALUES (42, 410, 'Este CROSS ORIGIN ACCESS si nu are nici un efect asupra unui request.', 0);
INSERT INTO raspunsuri
VALUES (42, 411, 'Este un protocol HTTP foarte bine securizat, care trimite datele dintr-un request encodate.', 0);
/
INSERT INTO domenii
VALUES (19, 'matematica');
INSERT INTO intrebari
VALUES (19, 43, 'Cat este 0/0?');
INSERT INTO raspunsuri
VALUES (43, 412, 'NaN', 1);
INSERT INTO raspunsuri
VALUES ('43', 413, 'Undefined', 1);
INSERT INTO raspunsuri
VALUES (43, 414, 'Null', 1);
INSERT INTO raspunsuri
VALUES (43, 415, 'Eroare', 1);
INSERT INTO raspunsuri
VALUES (43, 416, '0', 0);
INSERT INTO raspunsuri
VALUES (43, 417, '1', 0);
INSERT INTO raspunsuri
VALUES (43, 418, 'Infinit', 0);
INSERT INTO raspunsuri
VALUES (43, 419, '2', 0);
INSERT INTO raspunsuri
VALUES (43, 420, '-1', 0);
INSERT INTO raspunsuri
VALUES (43, 421, '-0', 0);
/
INSERT INTO intrebari
VALUES (19, 44, 'Cat este 1978.2/0.1?');
INSERT INTO raspunsuri
VALUES (44, 422, '22', 0);
INSERT INTO raspunsuri
VALUES (44, 423, '0', 0);
INSERT INTO raspunsuri
VALUES (44, 424, '-1', 0);
INSERT INTO raspunsuri
VALUES (44, 425, '4', 0);
INSERT INTO raspunsuri
VALUES (44, 426, '19.78', 1);
INSERT INTO raspunsuri
VALUES (44, 427, '5', 0);
INSERT INTO raspunsuri
VALUES (44, 428, '2', 0);
INSERT INTO raspunsuri
VALUES (44, 429, '19', 0);
INSERT INTO raspunsuri
VALUES (44, 430, '-19', 0);
INSERT INTO raspunsuri
VALUES (44, 431, '20', 0);
/
INSERT INTO intrebari
VALUES (19, 45, 'Cu cat este egal trei la pătrat?');
INSERT INTO raspunsuri
VALUES (45, 432, '2', 0);
INSERT INTO raspunsuri
VALUES (45, 433, '4', 0);
INSERT INTO raspunsuri
VALUES (45, 434, '9', 1);
INSERT INTO raspunsuri
VALUES (45, 435, '3', 0);
INSERT INTO raspunsuri
VALUES (45, 436, '-3', 0);
INSERT INTO raspunsuri
VALUES (45, 437, '6', 0);
INSERT INTO raspunsuri
VALUES (45, 438, '5', 0);
INSERT INTO raspunsuri
VALUES (45, 439, '10', 0);
INSERT INTO raspunsuri
VALUES (45, 440, '33', 0);
INSERT INTO raspunsuri
VALUES (45, 441, '100', 0);
/
INSERT INTO intrebari
VALUES (15, 46, 'Care este prima planeta de la Soare?');
INSERT INTO raspunsuri
VALUES (46, 442, 'Terra', 0);
INSERT INTO raspunsuri
VALUES (46, 443, 'Marte', 0);
INSERT INTO raspunsuri
VALUES (46, 444, 'Mercur', 1);
INSERT INTO raspunsuri
VALUES (46, 445, 'Ceres', 0);
INSERT INTO raspunsuri
VALUES (46, 446, 'Pluto', 0);
INSERT INTO raspunsuri
VALUES (46, 447, 'Saturn', 0);
INSERT INTO raspunsuri
VALUES (46, 448, 'Uranus', 0);
INSERT INTO raspunsuri
VALUES (46, 449, 'Neptun', 0);
INSERT INTO raspunsuri
VALUES (46, 450, 'Luna', 0);
INSERT INTO raspunsuri
VALUES (46, 451, 'Soare', 0);
/
INSERT INTO intrebari
VALUES (15, 47, 'Cati sateliti naturali are planeta Pamant?');
INSERT INTO raspunsuri
VALUES (47, 452, '2', 0);
INSERT INTO raspunsuri
VALUES (47, 453, '0', 0);
INSERT INTO raspunsuri
VALUES (47, 454, '4', 0);
INSERT INTO raspunsuri
VALUES (47, 455, '1', 1);
INSERT INTO raspunsuri
VALUES (47, 456, '3', 0);
INSERT INTO raspunsuri
VALUES (47, 457, '10', 0);
INSERT INTO raspunsuri
VALUES (47, 458, '5', 0);
INSERT INTO raspunsuri
VALUES (47, 459, '8', 0);
