SET SERVEROUTPUT ON;
DECLARE
    v_studenti_nume studenti.nume%TYPE := &i_studenti_nume;
    v_count_studenti NUMBER DEFAULT 0;
    v_studenti_id studenti.id%TYPE DEFAULT 0;
    v_studenti_prenume studenti.prenume%TYPE DEFAULT '';
    v_note_valoare_maxima note.valoare%TYPE DEFAULT 0;
    v_note_valoare_minima note.valoare%TYPE DEFAULT 0;
BEGIN
    SELECT COUNT(id) INTO v_count_studenti FROM studenti WHERE nume = v_studenti_nume;
    IF (v_count_studenti = 0)
        THEN
            DBMS_OUTPUT.PUT_LINE('Nu exista niciun student cu numele ''' || v_studenti_nume || '''.');
        ELSE
            DBMS_OUTPUT.PUT_LINE('Exista ' || v_count_studenti || ' studenti cu numele ''' || v_studenti_nume || '''.');
            SELECT id, prenume INTO v_studenti_id, v_studenti_prenume FROM
            (SELECT id, prenume FROM studenti WHERE nume = v_studenti_nume ORDER BY prenume) WHERE ROWNUM = 1;
            DBMS_OUTPUT.PUT_LINE('Studentul ' || v_studenti_id || ', cu prenumele ''' || v_studenti_prenume || ''', este primul student cu numele ''' || v_studenti_nume || '''.');
            SELECT MAX(valoare) INTO v_note_valoare_maxima FROM note WHERE id_student = v_studenti_id;
            SELECT MIN(valoare) INTO v_note_valoare_minima FROM note WHERE id_student = v_studenti_id;
            DBMS_OUTPUT.PUT_LINE('Cea mai mare nota a lui ''' || v_studenti_nume || ' ' || v_studenti_prenume
            || ''' este ' || v_note_valoare_maxima || '.');
            DBMS_OUTPUT.PUT_LINE('Cea mai mica nota a lui ''' || v_studenti_nume || ' ' || v_studenti_prenume
            || ''' este ' || v_note_valoare_minima || '.');
            DBMS_OUTPUT.PUT_LINE(v_note_valoare_maxima || ' la puterea ' || v_note_valoare_minima
            || ' este ' || (v_note_valoare_maxima ** v_note_valoare_minima) || '.');
    END IF;
END;