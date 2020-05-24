SET SERVEROUTPUT ON;

GRANT EXECUTE ON UTL_FILE TO STUDENT; -- SYSDBA
/
GRANT CREATE ANY DIRECTORY TO STUDENT; -- SYSDBA
/
CREATE OR REPLACE DIRECTORY Tema_8 AS 'D:\Anul II\Semestrul II\Practica SGBD\Teme\Tema 8';
/
GRANT READ, WRITE ON DIRECTORY TEMA_8 TO STUDENT; -- SYSDBA
/
CREATE OR REPLACE PROCEDURE exporta_catalog AS
    v_fisier UTL_FILE.FILE_TYPE;
    v_count  NUMBER DEFAULT 0;
BEGIN
    v_fisier := UTL_FILE.FOPEN('TEMA_8', 'catalog.xml', 'W');
    UTL_FILE.PUT_LINE(v_fisier, '<catalog>');
    FOR intrare IN (SELECT nr_matricol,
                           nume,
                           prenume,
                           s.an an_student,
                           grupa,
                           bursa,
                           data_nastere,
                           email,
                           valoare,
                           data_notare,
                           titlu_curs,
                           c.an an_curs,
                           semestru,
                           credite
                    FROM studenti s
                             JOIN note n ON s.id = n.id_student
                             JOIN cursuri c ON n.id_curs = c.id
                    ORDER BY nr_matricol)
        LOOP
            UTL_FILE.PUT_LINE(v_fisier, '<mark>');
            UTL_FILE.PUT_LINE(v_fisier, '<identifier>' || intrare.nr_matricol || '</identifier>');
            UTL_FILE.PUT_LINE(v_fisier, '<firstName>' || intrare.prenume || '</firstName>');
            UTL_FILE.PUT_LINE(v_fisier, '<lastName>' || intrare.nume || '</lastName>');
            UTL_FILE.PUT_LINE(v_fisier, '<studentYear>' || intrare.an_student || '</studentYear>');
            UTL_FILE.PUT_LINE(v_fisier, '<group>' || intrare.grupa || '</group>');
            UTL_FILE.PUT_LINE(v_fisier, '<scholarship>' || intrare.bursa || '</scholarship>');
            UTL_FILE.PUT_LINE(v_fisier, '<birthDate>' || intrare.data_nastere || '</birthDate>');
            UTL_FILE.PUT_LINE(v_fisier, '<email>' || intrare.email || '</email>');
            UTL_FILE.PUT_LINE(v_fisier, '<value>' || intrare.valoare || '</value>');
            UTL_FILE.PUT_LINE(v_fisier, '<markDate>' || intrare.data_notare || '</markDate>');
            UTL_FILE.PUT_LINE(v_fisier, '<course>' || intrare.titlu_curs || '</course>');
            UTL_FILE.PUT_LINE(v_fisier, '<courseYear>' || intrare.an_curs || '</courseYear>');
            UTL_FILE.PUT_LINE(v_fisier, '<semester>' || intrare.semestru || '</semester>');
            UTL_FILE.PUT_LINE(v_fisier, '<credits>' || intrare.credite || '</credits>');
            UTL_FILE.PUT_LINE(v_fisier, '</mark>');
            v_count := v_count + 1;
        END LOOP;
    UTL_FILE.PUT_LINE(v_fisier, '</catalog>');
    DBMS_OUTPUT.PUT_LINE(v_count || ' note au fost exportate în fișierul "catalog.xml".');
    UTL_FILE.FCLOSE(v_fisier);
EXCEPTION
    WHEN UTL_FILE.INVALID_FILEHANDLE
        THEN DBMS_OUTPUT.PUT_LINE('Fișierul nu a fost deschis.');
    WHEN UTL_FILE.INVALID_OPERATION
        THEN DBMS_OUTPUT.PUT_LINE('Fișierul este deja deschis din altă parte.');
END exporta_catalog;
/
BEGIN
    exporta_catalog();
END;
