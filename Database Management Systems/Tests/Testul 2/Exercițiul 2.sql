SET SERVEROUTPUT ON;
DECLARE
     v_number       NUMBER  := &i_number;
     v_factorial    NUMBER  DEFAULT 1;
BEGIN
    IF v_number = 0
        THEN
            DBMS_OUTPUT.PUT_LINE('1');
    END IF;
    FOR v_value IN 1..v_number LOOP
        v_factorial := v_factorial * v_value;
        DBMS_OUTPUT.PUT_LINE(v_factorial);
    END LOOP;
END;