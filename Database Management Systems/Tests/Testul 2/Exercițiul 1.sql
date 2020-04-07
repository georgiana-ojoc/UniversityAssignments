SET SERVEROUTPUT ON;
DECLARE
    v_number    NUMBER  := &i_number;
    v_ok        BOOLEAN DEFAULT TRUE;
BEGIN
    IF v_number < 2
        THEN
            DBMS_OUTPUT.PUT_LINE('Nu este prim.');
        ELSE
            FOR v_divisor IN 2..v_number / 2 LOOP
                IF v_number MOD v_divisor = 0
                    THEN
                        v_ok := FALSE;
                END IF;
            END LOOP;
            IF v_ok = TRUE
                THEN
                    DBMS_OUTPUT.PUT_LINE('Este prim.');
                ELSE
                    DBMS_OUTPUT.PUT_LINE('Nu este prim.');
            END IF;
    END IF;
END;