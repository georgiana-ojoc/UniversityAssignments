SET SERVEROUTPUT ON;

GRANT EXECUTE ON UTL_FILE TO STUDENT; -- SYSDBA
/
GRANT CREATE ANY DIRECTORY TO STUDENT; -- SYSDBA
/
CREATE OR REPLACE DIRECTORY Laboratorul_9 AS 'D:\Anul II\Semestrul II\Practica SGBD\Laboratoare\Laboratorul 9';
/
GRANT READ, WRITE ON DIRECTORY LABORATORUL_9 TO STUDENT; -- SYSDBA
/
DECLARE
    v_fisier UTL_FILE.FILE_TYPE;
    v_sir VARCHAR2(50);
BEGIN
    v_fisier := UTL_FILE.FOPEN('LABORATORUL_9', 'test.txt', 'W');
    UTL_FILE.PUTF(v_fisier, 'Hello!');
    UTL_FILE.FCLOSE(v_fisier);
    v_fisier := UTL_FILE.FOPEN('LABORATORUL_9', 'test.txt', 'R');
    UTL_FILE.GET_LINE(v_fisier, v_sir);
    DBMS_OUTPUT.PUT_LINE(v_sir);
    UTL_FILE.FCLOSE(v_fisier);
END;
