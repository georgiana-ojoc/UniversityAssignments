/* 1 */
SET SERVEROUTPUT ON;
BEGIN
   DBMS_OUTPUT.PUT_LINE('Maine: ' || (SYSDATE + 1));
   DBMS_OUTPUT.PUT_LINE(TO_CHAR(SYSDATE, 'Month DD, YYYY'));
END;



/* 2 */
SET SERVEROUTPUT ON;
DECLARE
      v_mesaj VARCHAR2(50) := 'Salutare, lume!';
BEGIN
      DBMS_OUTPUT.PUT_LINE('Mesaj: ' || v_mesaj);
END;



/* 3 */
DECLARE
    v_#cartofi NUMBER := 2E4;
    v_varsta int := &i_varsta;
    v_nume_student VARCHAR2(20) := &i_nume;
    v_nume_film VARCHAR2(30) := 'The Matrix';
    c_pi CONSTANT DOUBLE PRECISION := 3.141592653;
    v_salut VARCHAR2(40) DEFAULT 'Bine ati venit!';
    v_data_de_nastere DATE;
    v_numar_studenti NUMBER(3) NOT NULL := 1;
    v_promovam_la_PSGBD BOOLEAN DEFAULT TRUE;
BEGIN
    DBMS_OUTPUT.PUT_LINE(v_#cartofi);
    DBMS_OUTPUT.PUT_LINE(v_varsta);
    DBMS_OUTPUT.PUT_LINE(v_nume_student);
    DBMS_OUTPUT.PUT_LINE(v_nume_film);
    DBMS_OUTPUT.PUT_LINE(c_pi);
    DBMS_OUTPUT.PUT_LINE(v_salut);
    DBMS_OUTPUT.PUT_LINE(v_data_de_nastere);
    DBMS_OUTPUT.PUT_LINE(v_numar_studenti);
    -- DBMS_OUTPUT.PUT_LINE(v_promovam_la_PSGBD);
END;



/* 4 */
SET SERVEROUTPUT ON;
DECLARE
   v_nume VARCHAR2(20) := 'Cristi';
   v_varsta INTEGER := 21;
BEGIN
   DBMS_OUTPUT.PUT_LINE('Valoarea variabilei v_nume este: ' || v_nume);
   DECLARE
      v_nume NUMBER(3) := 5;
   BEGIN
         DBMS_OUTPUT.PUT_LINE('Valoarea variabilei v_nume este: ' || v_nume);
         DBMS_OUTPUT.PUT_LINE('Varsta este: ' || v_varsta);
   END;
   DBMS_OUTPUT.PUT_LINE('Valoarea variabilei v_nume este: ' || v_nume);
END;



/* 5 */
<<global>>
DECLARE
   v_nume VARCHAR2(20) := 'Cristi';
BEGIN
   DBMS_OUTPUT.PUT_LINE('Valoarea variabilei v_nume este: ' || v_nume);
   DECLARE
      v_nume NUMBER(3) := 5;
   BEGIN
         DBMS_OUTPUT.PUT_LINE('Valoarea variabilei v_nume este: ' || v_nume);
         DBMS_OUTPUT.PUT_LINE('Valoarea variabilei v_nume este: ' || global.v_nume);
   END;
   DBMS_OUTPUT.PUT_LINE('Valoarea variabilei v_nume este: ' || v_nume);
END;



/* 6 */
DECLARE
   v_text VARCHAR2(200);
BEGIN   
   v_text := 'La steaua care a rasarit 
E-o cale atat de lunga,
Ca mii de ani i-au trebuit,
Luminii, sa ne-ajunga';
   DBMS_OUTPUT.PUT_LINE(v_text);   
END;



/* 7 */
SET SERVEROUTPUT ON;
DECLARE
   a NUMBER := 10;
   b NUMBER := 4;
BEGIN
   DBMS_OUTPUT.PUT_LINE('Suma: ' || (a+b));
   DBMS_OUTPUT.PUT_LINE('Diferenta: ' || (a-b));
   DBMS_OUTPUT.PUT_LINE('Produsul: ' || (a*b));
   DBMS_OUTPUT.PUT_LINE('Impartirea: ' || (a/b));
   DBMS_OUTPUT.PUT_LINE('Exponentierea: ' || (a ** b));
END;



/* 8 */
SET SERVEROUTPUT ON;
DECLARE
   a NUMBER := 10;
   b NUMBER := 4;
BEGIN
   IF (a > b) 
      THEN DBMS_OUTPUT.PUT_LINE(a || ' > ' || b);
      ELSE DBMS_OUTPUT.PUT_LINE(b || ' >= ' || a);
   END IF;
   IF (a != b)
      THEN DBMS_OUTPUT.PUT_LINE('Cele doua valori sunt diferite.');
      ELSE DBMS_OUTPUT.PUT_LINE('Cele doua valori sunt egale.');
   END IF;   
END;



/* 9 */
SET SERVEROUTPUT ON;
DECLARE
   a NUMBER := TRUNC(DBMS_RANDOM.VALUE(0,100));
BEGIN
   IF (a IS NOT NULL) 
      THEN 
         IF (a >= 20 AND a <= 80)
              THEN DBMS_OUTPUT.PUT_LINE(a || ' este intre 20 si 80');
              ELSE DBMS_OUTPUT.PUT_LINE(a || ' NU este intre 20 si 80');
         END IF;     
      ELSE DBMS_OUTPUT.PUT_LINE('Valoare nula');
   END IF;
END;



/* 10 */
SET SERVEROUTPUT ON;
DECLARE
    v_nume VARCHAR2(30);
    v_prenume VARCHAR2(30);
    v_medie FLOAT(7);
BEGIN
    SELECT nume INTO v_nume FROM studenti WHERE ROWNUM = 1;
    DBMS_OUTPUT.PUT_LINE('Nume: ' || v_nume);
    SELECT AVG(valoare) INTO v_medie FROM note;
    DBMS_OUTPUT.PUT_LINE('Medie: ' || v_medie);
    SELECT nume, prenume INTO v_nume, v_prenume FROM studenti WHERE ROWNUM = 1;
    DBMS_OUTPUT.PUT_LINE('Nume si prenume: ' || v_nume || ' ' || v_prenume);
END;



/* 11 */
SET SERVEROUTPUT ON;
DECLARE
    v_note_valoare_maxima note.valoare%TYPE;
    v_note_valoare_minima v_note_valoare_maxima%TYPE;
BEGIN
    SELECT MAX(valoare) INTO v_note_valoare_maxima FROM note;
    DBMS_OUTPUT.PUT_LINE('Nota maxima: ' || v_note_valoare_maxima);
    SELECT MIN(valoare) INTO v_note_valoare_minima FROM note;
    DBMS_OUTPUT.PUT_LINE('Nota minima: ' || v_note_valoare_minima);
END;



/* 12 */
-- ultimul student din catalog
SET SERVEROUTPUT ON;
DECLARE
  v_id          studenti.id%TYPE;
  v_nume        studenti.nume%TYPE;
  v_prenume     studenti.prenume%TYPE;
BEGIN
  SELECT id, nume, prenume INTO v_id, v_nume, v_prenume FROM studenti WHERE id = (SELECT MAX(id) FROM studenti);
  DBMS_OUTPUT.PUT_LINE(v_id || ' ' || v_nume || ' ' || v_prenume);
END;



/* 13 */
-- student random
SET SERVEROUTPUT ON;
DECLARE
  v_nume_complet    VARCHAR2(100);
BEGIN
  SELECT nume || ' ' || prenume INTO v_nume_complet FROM
  (SELECT nume, prenume FROM studenti ORDER BY DBMS_RANDOM.VALUE) WHERE ROWNUM = 1;
  DBMS_OUTPUT.PUT_LINE(v_nume_complet);
END;



/* 14 */
-- studentul cu cel mai lung nume
SET SERVEROUTPUT ON;
DECLARE 
  v_nume_complet    VARCHAR2(100);
BEGIN
  SELECT nume || ' ' || prenume INTO v_nume_complet FROM
  (SELECT nume, prenume FROM studenti ORDER BY LENGTH(nume || ' ' || prenume) DESC) WHERE ROWNUM = 1;
  DBMS_OUTPUT.PUT_LINE(INITCAP(v_nume_complet) || ': ' || LENGTH(v_nume_complet) || ' caractere');
END;