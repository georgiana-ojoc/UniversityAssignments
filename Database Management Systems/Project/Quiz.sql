SET SERVEROUTPUT ON;

CREATE OR REPLACE PROCEDURE drop_if_exists(p_name VARCHAR2, p_type VARCHAR2) AS
    v_count        NUMBER DEFAULT -1;
    v_cursor       NUMBER DEFAULT -1;
    v_return_value NUMBER DEFAULT -1;
BEGIN
    CASE
        WHEN LOWER(p_type) = 'table' THEN
            SELECT COUNT(*) INTO v_count FROM user_tables WHERE UPPER(table_name) = UPPER(p_name);
        WHEN LOWER(p_type) = 'constraint' THEN
            SELECT COUNT(*) INTO v_count FROM user_constraints WHERE UPPER(constraint_name) = UPPER(p_name);
        WHEN LOWER(p_type) = 'index' THEN
            SELECT COUNT(*) INTO v_count FROM user_indexes WHERE UPPER(index_name) = UPPER(p_name);
        WHEN LOWER(p_type) = 'package' THEN
            SELECT COUNT(*) INTO v_count FROM user_procedures WHERE UPPER(object_name) = UPPER(p_name);
        WHEN LOWER(p_type) = 'procedure' THEN
            SELECT COUNT(*) INTO v_count FROM user_procedures WHERE UPPER(object_name) = UPPER(p_name);
        WHEN LOWER(p_type) = 'function' THEN
            SELECT COUNT(*) INTO v_count FROM user_procedures WHERE UPPER(object_name) = UPPER(p_name);
        WHEN LOWER(p_type) = 'type' THEN
            SELECT COUNT(*) INTO v_count FROM user_types WHERE UPPER(type_name) = UPPER(p_name);
        WHEN LOWER(p_type) = 'trigger' THEN
            SELECT COUNT(*) INTO v_count FROM user_triggers WHERE UPPER(trigger_name) = UPPER(p_name);
        WHEN LOWER(p_type) = 'view' THEN
            SELECT COUNT(*) INTO v_count FROM user_views WHERE UPPER(view_name) = UPPER(p_name);
        END CASE;
    IF v_count = 1 THEN
        v_cursor := DBMS_SQL.OPEN_CURSOR;
        DBMS_SQL.PARSE(v_cursor, 'DROP ' || p_type || ' ' || p_name, DBMS_SQL.NATIVE);
        v_return_value := DBMS_SQL.EXECUTE(v_cursor);
    END IF;
END drop_if_exists;
/
CREATE OR REPLACE PROCEDURE create_test(p_email IN VARCHAR2) AS
    v_cursor                      NUMBER DEFAULT -1;
    v_return_value                NUMBER DEFAULT -1;
    v_count                       NUMBER DEFAULT -1;
    v_index                       NUMBER DEFAULT -1;
    TYPE t_domains IS TABLE OF domenii.id%TYPE;
    v_domains                     t_domains;
    v_random_domain_index         NUMBER DEFAULT -1;
    TYPE t_selected_domains IS TABLE OF domenii.id%TYPE;
    v_selected_domains            t_selected_domains        := t_selected_domains();
    v_domain                      domenii.id%TYPE DEFAULT -1;
    v_questions_size              NUMBER DEFAULT -1;
    v_random_question_index       NUMBER DEFAULT -1;
    TYPE t_questions IS TABLE OF intrebari.id%TYPE;
    v_questions                   t_questions               := t_questions();
    v_correct_answers_size        NUMBER DEFAULT -1;
    v_incorrect_answers_size      NUMBER DEFAULT -1;
    v_random_correct_answer_index NUMBER DEFAULT -1;
    TYPE t_correct_answers_indexes IS TABLE OF NUMBER;
    v_correct_answers_indexes     t_correct_answers_indexes := t_correct_answers_indexes();
    v_correct                     NUMBER DEFAULT -1;
    v_random_answer_index         NUMBER DEFAULT -1;
    TYPE t_answers IS TABLE OF raspunsuri.id%TYPE;
    v_correct_answers             t_answers                 := t_answers();
    v_incorrect_answers           t_answers                 := t_answers();
    v_answer                      raspunsuri.id%TYPE DEFAULT -1;
    v_answers                     t_answers                 := t_answers();
    v_first_correct_answer        BOOLEAN DEFAULT TRUE;
    v_correct_answers_csv         VARCHAR2(1024) DEFAULT NULL;
BEGIN
    -- Creez tabelul.
    v_cursor := DBMS_SQL.OPEN_CURSOR;
    DBMS_SQL.PARSE(v_cursor, 'CREATE TABLE ' || p_email ||
                             ' (id NUMBER NOT NULL PRIMARY KEY, ' ||
                             'id_intrebare NUMBER NOT NULL, ' ||
                             'id_raspuns_1 NUMBER NOT NULL, ' ||
                             'id_raspuns_2 NUMBER NOT NULL, ' ||
                             'id_raspuns_3 NUMBER NOT NULL, ' ||
                             'id_raspuns_4 NUMBER NOT NULL, ' ||
                             'id_raspuns_5 NUMBER NOT NULL, ' ||
                             'id_raspuns_6 NUMBER NOT NULL, ' ||
                             'numar_raspunsuri_corecte NUMBER NOT NULL, ' ||
                             'raspunsuri_corecte VARCHAR2(1024) NOT NULL, ' ||
                             'raspunsuri_utilizator VARCHAR2(1024) DEFAULT NULL)',
                   DBMS_SQL.NATIVE);
    v_return_value := DBMS_SQL.EXECUTE(v_cursor);

    -- Salvez toate domeniile.
    SELECT id BULK COLLECT INTO v_domains FROM domenii ORDER BY id;

    -- Selectez 10 domenii diferite.
    v_selected_domains.EXTEND(10);
    FOR c_selected_domain IN 1 .. 10
        LOOP
            v_random_domain_index := TRUNC(DBMS_RANDOM.VALUE(1, v_domains.COUNT + 1));
            v_index := v_domains.FIRST;
            v_count := 1;
            LOOP
                IF v_count = v_random_domain_index THEN
                    EXIT;
                END IF;
                v_index := v_domains.next(v_index);
                v_count := v_count + 1;
            END LOOP;
            -- Cât timp a fost selectat un domeniu existent, aleg altul.
            WHILE v_domains(v_index) MEMBER OF v_selected_domains
                LOOP
                    v_random_domain_index := TRUNC(DBMS_RANDOM.VALUE(1, v_domains.COUNT + 1));
                    v_index := v_domains.FIRST;
                    v_count := 1;
                    LOOP
                        IF v_count = v_random_domain_index THEN
                            EXIT;
                        END IF;
                        v_index := v_domains.next(v_index);
                        v_count := v_count + 1;
                    END LOOP;
                END LOOP;
            v_selected_domains(c_selected_domain) := v_domains(v_index);
            -- Șterg din tabelul inițial domeniul selectat, pentru a evita să fie ales încă o dată.
            v_domains.DELETE(v_index);
        END LOOP;

    -- Selectez câte o întrebare din cele 10 domenii alese anterior.
    v_questions.extend(10);
    FOR c_question IN 1 .. 10
        LOOP
            v_domain := v_selected_domains(c_question);
            SELECT COUNT(*) INTO v_questions_size FROM intrebari WHERE id_domeniu = v_domain;
            v_random_question_index := TRUNC(DBMS_RANDOM.VALUE(1, v_questions_size + 1));
            SELECT id
            INTO v_questions(c_question)
            FROM (SELECT id, ROWNUM rand
                  FROM (SELECT id
                        FROM intrebari
                        WHERE id_domeniu = v_domain
                        ORDER BY id)
                  WHERE ROWNUM <= v_random_question_index)
            WHERE rand = v_random_question_index;
        END LOOP;

    -- Inserez 10 tuple în tabelul nou creat.
    FOR c_record IN 1 .. 10
        LOOP
            -- Determin câte răspunsuri corecte pentru întrebarea curentă sunt.
            SELECT COUNT(*)
            INTO v_correct_answers_size
            FROM raspunsuri
            WHERE id_intrebare = v_questions(c_record)
              AND corect = 1;

            -- Determin câte răspunsuri incorecte pentru întrebarea curentă sunt.
            SELECT COUNT(*)
            INTO v_incorrect_answers_size
            FROM raspunsuri
            WHERE id_intrebare = v_questions(c_record)
              AND corect = 0;

            -- Stabilesc câte raspunsuri corecte să conțină tuplul care va fi inserat în tabelul nou creat.
            IF v_incorrect_answers_size < 5 THEN
                v_correct_answers_size := 6 - v_incorrect_answers_size;
            ELSE
                v_correct_answers_size := TRUNC(DBMS_RANDOM.VALUE(1, v_correct_answers_size + 1));
                IF v_correct_answers_size > 5 THEN
                    v_correct_answers_size := 5;
                END IF;
            END IF;

            -- Stabilesc pe care dintre cele șase poziții se vor afla răspunsurile corecte.
            v_correct_answers_indexes := t_correct_answers_indexes();
            v_correct_answers_indexes.EXTEND(v_correct_answers_size);
            FOR c_correct_answer IN 1 .. v_correct_answers_size
                LOOP
                    v_random_correct_answer_index := TRUNC(DBMS_RANDOM.VALUE(1, 7));
                    WHILE v_random_correct_answer_index MEMBER OF v_correct_answers_indexes
                        LOOP
                            v_random_correct_answer_index := TRUNC(DBMS_RANDOM.VALUE(1, 7));
                        END LOOP;
                    v_correct_answers_indexes(c_correct_answer) := v_random_correct_answer_index;
                END LOOP;

            -- Selectez toate răspunsurile corecte pentru întrebarea curentă.
            SELECT id
                BULK COLLECT
            INTO v_correct_answers
            FROM raspunsuri
            WHERE id_intrebare = v_questions(c_record)
              AND corect = 1
            ORDER BY id;

            -- Selectez toate răspunsurile incorecte pentru întrebarea curentă.
            SELECT id
                BULK COLLECT
            INTO v_incorrect_answers
            FROM raspunsuri
            WHERE id_intrebare = v_questions(c_record)
              AND corect = 0
            ORDER BY id;

            -- Selectez șase răspunsuri corecte și incorecte diferite.
            v_answers := t_answers();
            v_answers.EXTEND(6);
            v_first_correct_answer := TRUE;
            v_correct_answers_csv := '';
            FOR c_answer IN 1 .. 6
                LOOP
                    IF c_answer MEMBER OF v_correct_answers_indexes THEN
                        v_correct := 1;
                        v_random_answer_index := TRUNC(DBMS_RANDOM.VALUE(1, v_correct_answers.COUNT + 1));
                        v_index := v_correct_answers.FIRST;
                        v_count := 1;
                        LOOP
                            IF v_count = v_random_answer_index THEN
                                EXIT;
                            END IF;
                            v_index := v_correct_answers.next(v_index);
                            v_count := v_count + 1;
                        END LOOP;
                        -- Cât timp a fost selectat un răspuns corect existent, aleg altul.
                        WHILE v_correct_answers(v_index) MEMBER OF v_answers
                            LOOP
                                v_random_answer_index := TRUNC(DBMS_RANDOM.VALUE(1, v_correct_answers.COUNT + 1));
                                v_index := v_correct_answers.FIRST;
                                v_count := 1;
                                LOOP
                                    IF v_count = v_random_answer_index THEN
                                        EXIT;
                                    END IF;
                                    v_index := v_correct_answers.next(v_index);
                                    v_count := v_count + 1;
                                END LOOP;
                            END LOOP;
                        v_answer := v_correct_answers(v_index);
                        -- Șterg din tabelul inițial răspunsul corect selectat, pentru a evita să fie ales încă o dată.
                        v_correct_answers.DELETE(v_index);
                    ELSE
                        v_correct := 0;
                        v_random_answer_index := TRUNC(DBMS_RANDOM.VALUE(1, v_incorrect_answers.COUNT + 1));
                        v_index := v_incorrect_answers.FIRST;
                        v_count := 1;
                        LOOP
                            IF v_count = v_random_answer_index THEN
                                EXIT;
                            END IF;
                            v_index := v_incorrect_answers.next(v_index);
                            v_count := v_count + 1;
                        END LOOP;
                        -- Cât timp a fost selectat un răspuns incorect existent, aleg altul.
                        WHILE v_incorrect_answers(v_index) MEMBER OF v_answers
                            LOOP
                                v_random_answer_index := TRUNC(DBMS_RANDOM.VALUE(1, v_incorrect_answers.COUNT + 1));
                                v_index := v_incorrect_answers.FIRST;
                                v_count := 1;
                                LOOP
                                    IF v_count = v_random_answer_index THEN
                                        EXIT;
                                    END IF;
                                    v_index := v_incorrect_answers.next(v_index);
                                    v_count := v_count + 1;
                                END LOOP;
                            END LOOP;
                        v_answer := v_incorrect_answers(v_index);
                        -- Șterg din tabelul inițial răspunsul incorect selectat, pentru a evita să fie ales încă o dată.
                        v_incorrect_answers.DELETE(v_index);
                    END IF;
                    v_answers(c_answer) := v_answer;

                    -- Concatenez răspunsul corect (dacă este cazul), la CSV-ul răspunsurilor corecte.
                    IF v_correct = 1 THEN
                        IF v_first_correct_answer = TRUE THEN
                            v_correct_answers_csv := v_correct_answers_csv || v_answer;
                            v_first_correct_answer := FALSE;
                        ELSE
                            v_correct_answers_csv := v_correct_answers_csv || ', ' || v_answer;
                        END IF;
                    END IF;
                END LOOP;

            -- Inserez noul tuplu în tabelul nou creat.
            DBMS_SQL.PARSE(v_cursor, 'INSERT INTO ' || p_email || ' VALUES (' ||
                                     c_record || ', ' || v_questions(c_record) || ', ' || v_answers(1) || ', ' ||
                                     v_answers(2) || ', ' || v_answers(3) || ', ' || v_answers(4) || ', ' ||
                                     v_answers(5) || ', ' || v_answers(6) || ', ' || v_correct_answers_size || ', ''' ||
                                     v_correct_answers_csv || ''', NULL)', DBMS_SQL.NATIVE);
            v_return_value := DBMS_SQL.EXECUTE(v_cursor);
        END LOOP;
END create_test;
/
CREATE OR REPLACE FUNCTION get_score(p_email VARCHAR2) RETURN VARCHAR2 AS
    v_cursor               NUMBER DEFAULT -1;
    v_return_value         NUMBER DEFAULT -1;
    v_correct_answers_size NUMBER DEFAULT -1;
    v_id_correct_answers   VARCHAR2(1024) DEFAULT NULL;
    v_id_user_answers      VARCHAR2(1024) DEFAULT NULL;
    v_question             intrebari.id%TYPE;
    v_answer               raspunsuri.id%TYPE;
    TYPE t_answers IS TABLE OF raspunsuri.id%TYPE;
    v_correct_answers      t_answers     := t_answers();
    v_user_answers         t_answers     := t_answers();
    v_count                NUMBER DEFAULT -1;
    v_answer_score         NUMBER(5, 2) DEFAULT -1;
    v_question_score       NUMBER(5, 2) DEFAULT -1;
    v_total_score          NUMBER(5, 2) DEFAULT 0;
    v_object               JSON_OBJECT_T := JSON_OBJECT_T();
    v_array                JSON_ARRAY_T  := JSON_ARRAY_T();
BEGIN
    v_cursor := DBMS_SQL.OPEN_CURSOR;
    FOR c_record IN 1 .. 10
        LOOP
            -- Selectez răspunsurile corecte și răspunsurile utilizatorului pentru întrebarea curentă.
            DBMS_SQL.PARSE(v_cursor, 'SELECT numar_raspunsuri_corecte, raspunsuri_corecte, ' ||
                                     'raspunsuri_utilizator ' ||
                                     'FROM ' || p_email ||
                                     ' WHERE id = :id', DBMS_SQL.NATIVE);
            DBMS_SQL.BIND_VARIABLE(v_cursor, 'id', c_record);
            DBMS_SQL.DEFINE_COLUMN(v_cursor, 1, v_correct_answers_size);
            DBMS_SQL.DEFINE_COLUMN(v_cursor, 2, v_id_correct_answers, 1024);
            DBMS_SQL.DEFINE_COLUMN(v_cursor, 3, v_id_user_answers, 1024);
            v_return_value := DBMS_SQL.EXECUTE_AND_FETCH(v_cursor);
            DBMS_SQL.COLUMN_VALUE(v_cursor, 1, v_correct_answers_size);
            DBMS_SQL.COLUMN_VALUE(v_cursor, 2, v_id_correct_answers);
            DBMS_SQL.COLUMN_VALUE(v_cursor, 3, v_id_user_answers);

            -- Extrag răspunsurile corecte într-un nested table.
            v_correct_answers := t_answers();
            v_correct_answers.EXTEND(v_correct_answers_size);
            v_count := 1;
            v_answer := TO_NUMBER(REGEXP_SUBSTR(v_id_correct_answers, '[^, ]+', 1, v_count));
            WHILE LENGTH(v_answer) > 0
                LOOP
                    v_correct_answers(v_count) := v_answer;
                    v_count := v_count + 1;
                    v_answer := TO_NUMBER(REGEXP_SUBSTR(v_id_correct_answers, '[^, ]+', 1, v_count));
                END LOOP;

            -- Extrag întrebarea într-o variabilă.
            v_question := TO_NUMBER(REGEXP_SUBSTR(v_id_user_answers, '[^: ]+', 1, 1));

            -- Extrag răspunsurile utilizatorului într-un nested table.
            v_user_answers := t_answers();
            v_count := 1;
            v_answer := TO_NUMBER(REGEXP_SUBSTR(v_id_user_answers, '[^, ]+', 1, v_count + 1));
            WHILE LENGTH(v_answer) > 0
                LOOP
                    v_user_answers.EXTEND(1);
                    v_user_answers(v_count) := v_answer;
                    v_count := v_count + 1;
                    v_answer := TO_NUMBER(REGEXP_SUBSTR(v_id_user_answers, '[^, ]+', 1, v_count + 1));
                END LOOP;

            -- Adaug în JSON întrebarea și răspunsurile corecte.
            v_object := JSON_OBJECT_T();
            v_object.PUT('intrebare_' || c_record, v_question);
            v_object.PUT('raspunsuri_corecte', v_id_correct_answers);

            -- Calculez punctajul fiecărui răspuns și îl adaug la punctajul întebării și în JSON.
            v_question_score := 0;
            FOR c_answer IN 1 .. v_user_answers.COUNT
                LOOP
                    IF v_user_answers(c_answer) MEMBER OF v_correct_answers THEN
                        v_answer_score := 10 / v_correct_answers_size;
                    ELSE
                        v_answer_score := -10 / v_correct_answers_size;
                    END IF;
                    v_question_score := v_question_score + v_answer_score;
                    v_object.PUT('raspuns_' || c_answer, v_user_answers(c_answer));
                    v_object.PUT('punctaj_' || c_answer, v_answer_score);
                END LOOP;

            -- Dacă punctajul întebării este negativ, nu îl iau în calcul.
            IF v_question_score < 0 THEN
                FOR c_answer IN 1 .. v_user_answers.COUNT
                    LOOP
                        v_object.PUT('punctaj_' || c_answer, 0);
                    END LOOP;
            ELSE
                v_total_score := v_total_score + v_question_score;
            END IF;

            v_array.APPEND(v_object);
        END LOOP;

    -- Adaug punctajul total în JSON.
    v_array.APPEND(JSON_OBJECT('total' VALUE v_total_score FORMAT JSON));

    -- Șterg testul pentru ca data viitoare să se genereze unul nou.
    drop_if_exists(p_email, 'table');

    RETURN v_array.TO_STRING();
EXCEPTION
    WHEN OTHERS
        THEN RETURN NULL;
END get_score;
/
CREATE OR REPLACE FUNCTION get_next_question(p_email VARCHAR2, p_raspuns VARCHAR2) RETURN VARCHAR2 AS
    v_email         VARCHAR2(128) DEFAULT NULL;
    v_teste         NUMBER DEFAULT 0;
    v_cursor        NUMBER;
    v_return_value  NUMBER DEFAULT -1;
    v_intrebari     NUMBER DEFAULT -1;
    v_id_intrebare  intrebari.id%TYPE DEFAULT -1;
    TYPE t_id_raspunsuri IS TABLE OF raspunsuri.id%TYPE;
    v_id_raspunsuri t_id_raspunsuri := t_id_raspunsuri();
    v_intrebare     intrebari.continut%TYPE DEFAULT NULL;
    TYPE t_raspunsuri IS TABLE OF raspunsuri.continut%TYPE;
    v_raspunsuri    t_raspunsuri    := t_raspunsuri();
BEGIN
    -- Elimin caracterele speciale și micșorez la maxim 30 de caractere numele viitorului tabel.
    v_email := SUBSTR(REGEXP_REPLACE(p_email, '[^a-zA-Z0-9]', ''), 1, 30);

    -- Determin câte teste asociate utilizatorului există în baza de date.
    SELECT COUNT(*) INTO v_teste FROM user_tables WHERE LOWER(table_name) = LOWER(v_email);

    -- Dacă nu există niciun test asociat utilizatorului, creez unul.
    IF v_teste = 0
    THEN
        create_test(v_email);
    END IF;

    -- Actualizez răspunsul în tabel.
    v_cursor := DBMS_SQL.OPEN_CURSOR;
    IF p_raspuns IS NOT NULL THEN
        DBMS_SQL.PARSE(v_cursor, 'UPDATE ' || v_email ||
                                 ' SET raspunsuri_utilizator = ''' || p_raspuns ||
                                 ''' WHERE id = ' ||
                                 '(SELECT id FROM ' ||
                                 '(SELECT id FROM ' || v_email ||
                                 ' ORDER BY raspunsuri_utilizator NULLS FIRST, id)' ||
                                 ' WHERE ROWNUM < 2)', DBMS_SQL.NATIVE);
        v_return_value := DBMS_SQL.EXECUTE(v_cursor);
    END IF;

    -- Determin numărul de întrebări la care nu a răspuns.
    DBMS_SQL.PARSE(v_cursor, 'SELECT COUNT(*) FROM ' || v_email ||
                             ' WHERE raspunsuri_utilizator IS NULL', DBMS_SQL.NATIVE);
    DBMS_SQL.DEFINE_COLUMN(v_cursor, 1, v_intrebari);
    v_return_value := DBMS_SQL.EXECUTE_AND_FETCH(v_cursor);
    DBMS_SQL.COLUMN_VALUE(v_cursor, 1, v_intrebari);

    -- Returnez prima întrebare la care nu a răspuns sau punctajul total.
    IF v_intrebari > 0 THEN
        -- Selectez identificatorii primei întrebări la care nu a răspuns.
        DBMS_SQL.PARSE(v_cursor,
                       'SELECT id_intrebare, ' ||
                       'id_raspuns_1, id_raspuns_2, id_raspuns_3, id_raspuns_4, id_raspuns_5, id_raspuns_6 FROM ' ||
                       '(SELECT id_intrebare, ' ||
                       'id_raspuns_1, id_raspuns_2, id_raspuns_3, id_raspuns_4, id_raspuns_5, id_raspuns_6 FROM ' ||
                       v_email ||
                       ' ORDER BY raspunsuri_utilizator NULLS FIRST, id) ' ||
                       'WHERE ROWNUM < 2',
                       DBMS_SQL.NATIVE);
        DBMS_SQL.DEFINE_COLUMN(v_cursor, 1, v_id_intrebare);
        v_id_raspunsuri.EXTEND(6);
        FOR c_raspuns IN 1 .. 6
            LOOP
                DBMS_SQL.DEFINE_COLUMN(v_cursor, c_raspuns + 1, v_id_raspunsuri(c_raspuns));
            END LOOP;
        v_return_value := DBMS_SQL.EXECUTE_AND_FETCH(v_cursor);
        DBMS_SQL.COLUMN_VALUE(v_cursor, 1, v_id_intrebare);
        FOR c_id_raspuns IN 1 .. 6
            LOOP
                DBMS_SQL.COLUMN_VALUE(v_cursor, c_id_raspuns + 1, v_id_raspunsuri(c_id_raspuns));
            END LOOP;

        -- Selectez informațiile corespunzătoare primei întrebări la care nu a răspuns.
        SELECT continut INTO v_intrebare FROM intrebari WHERE id = v_id_intrebare;
        v_raspunsuri.EXTEND(6);
        FOR c_raspuns IN 1 .. 6
            LOOP
                SELECT continut INTO v_raspunsuri(c_raspuns) FROM raspunsuri WHERE id = v_id_raspunsuri(c_raspuns);
            END LOOP;

        -- Construiesc JSON-ul.
        RETURN JSON_OBJECT('id_intrebare' VALUE v_id_intrebare,
                           'intrebare' VALUE v_intrebare,
                           'id_raspuns_1' VALUE v_id_raspunsuri(1),
                           'raspuns_1' VALUE v_raspunsuri(1),
                           'id_raspuns_2' VALUE v_id_raspunsuri(2),
                           'raspuns_2' VALUE v_raspunsuri(2),
                           'id_raspuns_3' VALUE v_id_raspunsuri(3),
                           'raspuns_3' VALUE v_raspunsuri(3),
                           'id_raspuns_4' VALUE v_id_raspunsuri(4),
                           'raspuns_4' VALUE v_raspunsuri(4),
                           'id_raspuns_5' VALUE v_id_raspunsuri(5),
                           'raspuns_5' VALUE v_raspunsuri(5),
                           'id_raspuns_6' VALUE v_id_raspunsuri(6),
                           'raspuns_6' VALUE v_raspunsuri(6)
                           FORMAT JSON);
    ELSE
        RETURN get_score(v_email);
    END IF;
END get_next_question;
/
BEGIN
    -- DBMS_OUTPUT.PUT_LINE(get_next_question('IONUAICRO', NULL));
    -- {"id_intrebare":12,"intrebare":"Care animal este mamifer?","id_raspuns_1":98,"raspuns_1":"Gaina","id_raspuns_2":101,"raspuns_2":"Delfinul","id_raspuns_3":102,"raspuns_3":"Broasca testoasa","id_raspuns_4":99,"raspuns_4":"Barza","id_raspuns_5":100,"raspuns_5":"Sarpele","id_raspuns_6":97,"raspuns_6":Ursul}
    -- DBMS_OUTPUT.PUT_LINE(get_next_question('IONUAICRO', NULL));
    -- {"id_intrebare":12,"intrebare":"Care animal este mamifer?","id_raspuns_1":98,"raspuns_1":"Gaina","id_raspuns_2":101,"raspuns_2":"Delfinul","id_raspuns_3":102,"raspuns_3":"Broasca testoasa","id_raspuns_4":99,"raspuns_4":"Barza","id_raspuns_5":100,"raspuns_5":"Sarpele","id_raspuns_6":97,"raspuns_6":Ursul}
    -- DBMS_OUTPUT.PUT_LINE(get_next_question('IONUAICRO', '12: 101, 97'));
    -- {"id_intrebare":3,"intrebare":"Care dintre urmatoarele afirmatii sunt adevarate?","id_raspuns_1":26,"raspuns_1":"Un poligon este o linie franta inchisa.","id_raspuns_2":21,"raspuns_2":"Triunghiul are patru laturi.","id_raspuns_3":24,"raspuns_3":"Suma unghiurilor unui patrat este de 180 de grade.","id_raspuns_4":22,"raspuns_4":"Cercul are o infinitate de varfuri.","id_raspuns_5":27,"raspuns_5":"Intr-un triunghi dreptunghic suma catetelor este egala cu ipotenuza.","id_raspuns_6":25,"raspuns_6":Un patrat este un romb cu un unghi drept.}
    -- DBMS_OUTPUT.PUT_LINE(get_next_question('IONUAICRO', '3: 26, 21'));
    -- {"id_intrebare":14,"intrebare":"Cine a fost Alexandru Ioan Cuza?","id_raspuns_1":115,"raspuns_1":"A fost un pictor roman.","id_raspuns_2":117,"raspuns_2":"A fost ultimul domnitor al Principatelor Unite si al statului national Romania.","id_raspuns_3":113,"raspuns_3":"A fost un om inrobit de doua patimi: iubirea pentru patrie si iubirea pentru femei frumoase.","id_raspuns_4":119,"raspuns_4":"A fost un domnitor roman nascut in anul 1859.","id_raspuns_5":116,"raspuns_5":"A fost primul scriitor de opera literara romana.","id_raspuns_6":120,"raspuns_6":A fost un domnitor intre anii 1859-1866.}
    -- DBMS_OUTPUT.PUT_LINE(get_next_question('IONUAICRO', '14: 116'));
    -- {"id_intrebare":40,"intrebare":"Care dintre urmatoarele culori nu sunt primare?","id_raspuns_1":393,"raspuns_1":"Gri","id_raspuns_2":396,"raspuns_2":"Oranj","id_raspuns_3":397,"raspuns_3":"Galben","id_raspuns_4":392,"raspuns_4":"Rosu","id_raspuns_5":395,"raspuns_5":"Albastru","id_raspuns_6":391,"raspuns_6":Verde}
    -- DBMS_OUTPUT.PUT_LINE(get_next_question('IONUAICRO', '40: 393, 396, 391'));
    -- {"id_intrebare":29,"intrebare":"Care este maestrul xiaolin?","id_raspuns_1":249,"raspuns_1":"Chase","id_raspuns_2":250,"raspuns_2":"Master Fung","id_raspuns_3":251,"raspuns_3":"Nu stiu.","id_raspuns_4":248,"raspuns_4":"Jack","id_raspuns_5":247,"raspuns_5":"Wuya","id_raspuns_6":246,"raspuns_6":Kimiko}
    -- DBMS_OUTPUT.PUT_LINE(get_next_question('IONUAICRO', '29: 249, 248, 247'));
    -- {"id_intrebare":42,"intrebare":"Ce este CORS si ce efect are o exceptie de acest tip asupra unui request?","id_raspuns_1":405,"raspuns_1":"Este CROSS ORIGIN ACCESS si previne un client din a face request catre un server pe un port diferit.","id_raspuns_2":409,"raspuns_2":"Este CROSS ORIGIN ACCESS si previne aplicatiile externe din a folosit un request.","id_raspuns_3":411,"raspuns_3":"Este un protocol HTTP foarte bine securizat, care trimite datele dintr-un request encodate.","id_raspuns_4":406,"raspuns_4":"Este CROISSANT OREO SECURIZAT si este un warning care spune ca requestul nu are sens.","id_raspuns_5":410,"raspuns_5":"Este CROSS ORIGIN ACCESS si nu are nici un efect asupra unui request.","id_raspuns_6":407,"raspuns_6":Este CORD ORIGIN ACCESS si este o exceptie care previne atacatorii din a fura date.}
    -- DBMS_OUTPUT.PUT_LINE(get_next_question('IONUAICRO', '42: 405'));
    -- {"id_intrebare":8,"intrebare":"Care flori sunt sau pot fi albe?","id_raspuns_1":69,"raspuns_1":"Floarea de cires","id_raspuns_2":66,"raspuns_2":"Trandafirul","id_raspuns_3":72,"raspuns_3":"Floarea soarelui","id_raspuns_4":68,"raspuns_4":"Papadia","id_raspuns_5":71,"raspuns_5":"Bumbisorul","id_raspuns_6":67,"raspuns_6":Floarea de soc}
    -- DBMS_OUTPUT.PUT_LINE(get_next_question('IONUAICRO', '8: 66, 67'));
    -- {"id_intrebare":37,"intrebare":"Ce organit celular este supranumit \"the powerhouse of the cell\"?","id_raspuns_1":375,"raspuns_1":"Aparatul Golgi","id_raspuns_2":370,"raspuns_2":"Mitocendria","id_raspuns_3":371,"raspuns_3":"Mitucondria","id_raspuns_4":369,"raspuns_4":"Mitocondria","id_raspuns_5":376,"raspuns_5":"Mitocondria, din nou","id_raspuns_6":374,"raspuns_6":Ribozomul}
    -- DBMS_OUTPUT.PUT_LINE(get_next_question('IONUAICRO', '37: 369, 376'));
    -- {"id_intrebare":15,"intrebare":"Care sunt scriitori romani?","id_raspuns_1":130,"raspuns_1":"Ion Andreescu","id_raspuns_2":128,"raspuns_2":"Nicolae Tonitza","id_raspuns_3":122,"raspuns_3":"Ion Luca Caragiale","id_raspuns_4":127,"raspuns_4":"Nicolae Grigorescu","id_raspuns_5":129,"raspuns_5":"Stefan Luchian","id_raspuns_6":125,"raspuns_6":Ion Creanga}
    -- DBMS_OUTPUT.PUT_LINE(get_next_question('IONUAICRO', '15: 122, 125'));
    -- {"id_intrebare":43,"intrebare":"Cat este 0/0?","id_raspuns_1":416,"raspuns_1":"0","id_raspuns_2":420,"raspuns_2":"-1","id_raspuns_3":421,"raspuns_3":"-0","id_raspuns_4":412,"raspuns_4":"NaN","id_raspuns_5":414,"raspuns_5":"Null","id_raspuns_6":417,"raspuns_6":1}
    -- DBMS_OUTPUT.PUT_LINE(get_next_question('IONUAICRO', '43: 412'));
    -- [{"intrebare_1":12,"raspunsuri_corecte":"101, 97","raspuns_1":101,"punctaj_1":5,"raspuns_2":97,"punctaj_2":5},{"intrebare_2":3,"raspunsuri_corecte":"26, 22, 25","raspuns_1":26,"punctaj_1":3.33,"raspuns_2":21,"punctaj_2":-3.33},{"intrebare_3":14,"raspunsuri_corecte":"113, 120","raspuns_1":116,"punctaj_1":0},{"intrebare_4":40,"raspunsuri_corecte":"396, 391","raspuns_1":393,"punctaj_1":-5,"raspuns_2":396,"punctaj_2":5,"raspuns_3":391,"punctaj_3":5},{"intrebare_5":29,"raspunsuri_corecte":"246","raspuns_1":249,"raspuns_2":248,"raspuns_3":247,"punctaj_1":0,"punctaj_2":0,"punctaj_3":0},{"intrebare_6":42,"raspunsuri_corecte":"405, 409","raspuns_1":405,"punctaj_1":5},{"intrebare_7":8,"raspunsuri_corecte":"69, 66, 71, 67","raspuns_1":66,"punctaj_1":2.5,"raspuns_2":67,"punctaj_2":2.5},{"intrebare_8":37,"raspunsuri_corecte":"369, 376","raspuns_1":369,"punctaj_1":5,"raspuns_2":376,"punctaj_2":5},{"intrebare_9":15,"raspunsuri_corecte":"122, 125","raspuns_1":122,"punctaj_1":5,"raspuns_2":125,"punctaj_2":5},{"intrebare_10":43,"raspunsuri_corecte":"412, 414","raspuns_1":412,"punctaj_1":5},"{\"total\":50}"]
END;
