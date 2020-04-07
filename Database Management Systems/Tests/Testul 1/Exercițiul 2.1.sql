SET SERVEROUTPUT ON;
DECLARE
    v_sir VARCHAR(20) := &i_sir;
    v_count_studenti NUMBER DEFAULT 0;
    v_random NUMBER DEFAULT 0;
    v_student_id studenti.id%TYPE DEFAULT 0;
BEGIN
    SELECT COUNT(id) INTO v_count_studenti FROM studenti WHERE nume LIKE '%' || v_sir || '%' OR prenume LIKE '%' || v_sir || '%';
    IF (v_count_studenti = 0)
        THEN
            DBMS_OUTPUT.PUT_LINE('Nu exista niciun student care are in componenta numelui sau a prenumelui sau sirul de caractere ''' || v_sir || '''.');
        ELSE
            DBMS_OUTPUT.PUT_LINE('Numar studenti care au in componenta numelui sau a prenumelui sau sirul de caractere ''' || v_sir || ''': ' || v_count_studenti);
            v_random := TRUNC(DBMS_RANDOM.VALUE(1, v_count_studenti));
            SELECT id INTO v_student_id FROM (SELECT id FROM (SELECT id FROM studenti WHERE nume LIKE '%' || v_sir || '%' OR prenume LIKE '%' || v_sir || '%' ORDER BY id) WHERE ROWNUM <= v_random ORDER BY id DESC) WHERE ROWNUM = 1;
            DBMS_OUTPUT.PUT_LINE('Studentul ' || v_random || ' care are in componenta numelui sau a prenumelui sau sirul de caractere ''' || v_sir || ''' este ' || v_student_id || '.');
    END IF;
END;

