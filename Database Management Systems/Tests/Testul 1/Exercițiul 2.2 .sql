SET SERVEROUTPUT ON;
DECLARE
    v_studenti_data_nastere_maxima studenti.data_nastere%TYPE DEFAULT SYSDATE;
    v_studenti_data_nastere_minima studenti.data_nastere%TYPE DEFAULT SYSDATE;
    v_diferenta NUMBER DEFAULT 0;
BEGIN
    SELECT MAX(data_nastere), MIN(data_nastere), MAX(data_nastere) - MIN(data_nastere)
    INTO v_studenti_data_nastere_maxima, v_studenti_data_nastere_minima, v_diferenta FROM studenti;
    DBMS_OUTPUT.PUT_LINE('Diferenta dintre ' || v_studenti_data_nastere_maxima || ' si ' || v_studenti_data_nastere_minima
    || ' este ' || v_diferenta || '.');
END;