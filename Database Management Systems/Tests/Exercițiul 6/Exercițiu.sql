SET SERVEROUTPUT ON;

BEGIN
    DROP_IF_EXISTS('buildings', 'table');
    DROP_IF_EXISTS('hotel', 'type');
END;
/
CREATE OR REPLACE TYPE building AS OBJECT
    (city       VARCHAR2(25),
    street      VARCHAR(50),
    identifier  VARCHAR2(4),
    floors      NUMBER(2),
    color       VARCHAR2(15),
    CONSTRUCTOR FUNCTION building(city VARCHAR2, street VARCHAR2, identifier VARCHAR2)
        RETURN SELF AS RESULT,
    ORDER MEMBER FUNCTION compare_floors(p_building BUILDING)
        RETURN NUMBER,
    MEMBER PROCEDURE add_floors(p_floors NUMBER),
    NOT FINAL MEMBER PROCEDURE show_information
) NOT FINAL;
/
CREATE OR REPLACE TYPE BODY building AS
    CONSTRUCTOR FUNCTION building(city VARCHAR2, street VARCHAR2, identifier VARCHAR2)
        RETURN SELF AS RESULT AS
    BEGIN
        SELF.city := city;
        SELF.street := street;
        SELF.identifier := identifier;
        SELF.floors := DBMS_RANDOM.VALUE(1, 11);
        SELF.color := 'white';
        RETURN;
    END building;
    ORDER MEMBER FUNCTION compare_floors(p_building BUILDING)
        RETURN NUMBER AS
    BEGIN
        IF floors < p_building.floors THEN RETURN -1;
        ELSIF floors = p_building.floors THEN RETURN 0;
        END IF;
        RETURN 1;
    END compare_floors;
    MEMBER PROCEDURE add_floors(p_floors NUMBER) AS
    BEGIN
        IF p_floors < 1
            THEN DBMS_OUTPUT.PUT_LINE('Invalid number of floors (it should be greater than 0).');
            ELSE floors := floors + p_floors;
        END IF;
    END add_floors;
    MEMBER PROCEDURE show_information AS
    BEGIN
        DBMS_OUTPUT.PUT_LINE('City: ' || city);
        DBMS_OUTPUT.PUT_LINE('Street: ' || street);
        DBMS_OUTPUT.PUT_LINE('Number: ' || identifier);
        DBMS_OUTPUT.PUT_LINE('Number of floors: ' || floors);
        DBMS_OUTPUT.PUT_LINE('Color: ' || color);
    END show_information;
END;
/
CREATE OR REPLACE TYPE hotel UNDER building
    (name   VARCHAR(25),
    stars   NUMBER(1),
    CONSTRUCTOR FUNCTION hotel(city VARCHAR2, street VARCHAR2, identifier VARCHAR2, name VARCHAR2)
        RETURN SELF AS RESULT,
    MEMBER PROCEDURE change_stars(p_stars NUMBER),
    OVERRIDING MEMBER PROCEDURE show_information
);
/
CREATE OR REPLACE TYPE BODY hotel AS
    CONSTRUCTOR FUNCTION hotel(city VARCHAR2, street VARCHAR2, identifier VARCHAR2, name VARCHAR2)
        RETURN SELF AS RESULT AS
    BEGIN
        SELF.city := city;
        SELF.street := street;
        SELF.identifier := identifier;
        SELF.name := name;
        SELF.stars := 5;
        SELF.floors := DBMS_RANDOM.VALUE(5, 11);
        SELF.color := 'white';
        RETURN;
    END hotel;
    MEMBER PROCEDURE change_stars(p_stars NUMBER) AS
    BEGIN
        IF stars + p_stars < 1
            THEN DBMS_OUTPUT.PUT_LINE('Invalid number of stars (the hotel should have at least one star).');
            ELSE stars := stars + p_stars;
        END IF;
    END change_stars;
    OVERRIDING MEMBER PROCEDURE show_information AS
    BEGIN
        DBMS_OUTPUT.PUT_LINE('City: ' || city);
        DBMS_OUTPUT.PUT_LINE('Street: ' || street);
        DBMS_OUTPUT.PUT_LINE('Number: ' || identifier);
        DBMS_OUTPUT.PUT_LINE('Name: ' || name);
        DBMS_OUTPUT.PUT_LINE('Number of stars: ' || stars);
        DBMS_OUTPUT.PUT_LINE('Number of floors: ' || floors);
        DBMS_OUTPUT.PUT_LINE('Color: ' || color);
    END show_information;
END;
/
CREATE TABLE buildings (identifier NUMBER(4), building BUILDING);
/
DECLARE
    v_building1    BUILDING;
    v_building2    BUILDING;
    v_hotel1       HOTEL;
    v_hotel2       HOTEL;
BEGIN
    v_building1 := building('Viena', 'Graben', '100A');
    v_building2 := building('Milano', 'Montenapoleone', '30B');
    v_hotel1 := hotel('Paris', 'Vendome', '15', 'Ritz');
    v_hotel2 := hotel('Rio de Janeiro', 'Atlantica', '150', 'Belmond Copacabana');
    v_building1.add_floors(-1);
    v_hotel1.change_stars(-10);
    v_building2.show_information();
    v_hotel2.show_information();
    INSERT INTO buildings VALUES (1, v_building1);
    INSERT INTO buildings VALUES (2, v_building2);
    INSERT INTO buildings VALUES (3, v_hotel1);
    INSERT INTO buildings VALUES (4, v_hotel2);
END;
/
SELECT * FROM buildings ORDER BY building;