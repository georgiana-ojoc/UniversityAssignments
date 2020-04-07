SET SERVEROUTPUT ON;

BEGIN
    drop_if_exists('books', 'table');
END;
/
CREATE TABLE books
    (id_book NUMBER(4) NOT NULL PRIMARY KEY,
    id_student INT NOT NULL,
    title VARCHAR2(50),
    author VARCHAR2(50),
    genre VARCHAR2(50),
    year NUMBER(4),
    pages NUMBER(4),
    CONSTRAINT fk_student_book FOREIGN KEY (id_student) REFERENCES studenti(id)
);
/
DECLARE
    TYPE t_list IS VARYING ARRAY(100) OF VARCHAR2(50);
    v_students      NUMBER;
    v_titles        t_list;
    v_authors       t_list;
    v_genres        t_list;
    v_id_student    books.id_student%TYPE;
    v_title         books.title%TYPE;
    v_author        books.author%TYPE;
    v_genre         books.genre%TYPE;
    v_year          books.year%TYPE;
    v_pages         books.pages%TYPE;
BEGIN
    v_titles := t_list('All Passion Spent', 'A Farewell to Arms', 'The Millstone', 'Everything is Illuminated', 'Of Human Bondage',
    'A Handful of Dust', 'The Way of All Flesh', 'What''s Become of Waring', 'Edna O''Brien', 'Dying of the Light',
    'Great Work of Time', 'Wildfire at Midnight', 'The Doors of Perception', 'It''s a Battlefield', 'Jacob Have I Loved',
    'A Darkling Plain', 'The Other Side of Silence', 'Cover Her Face', 'Alone on a Wide, Wide Sea', 'Pale Kings and Princes',
    'For Whom the Bell Tolls', 'Fame Is the Spur', 'The Widening Gyre', 'Death Be Not Proud', 'The Sun Also Rises',
    'I Sing the Body Electric', 'Such, Such Were the Joys', 'The Wealth of Nations', 'The Lathe of Heaven', 'Paths of Glory',
    'The Painted Veil', 'By Grand Central Station I Sat Down and Wept', 'Time of our Darkness', 'Things Fall Apart', 'In a Dry Season',
    'Blithe Spirit', 'Tirra Lirra by the River', 'O Pioneers!', 'Have His Carcase', 'Arms and the Man',
    'The Mermaids Singing', 'This Side of Paradise', 'Where Angels Fear to Tread', 'In Death Ground', 'As I Lay Dying',
    'Recalled to Life', 'A Many-Splendoured Thing', 'The Man Within', 'Postern of Fate', 'Brandy of the Damned',
    'The Violent Bear It Away', 'Fair Stood the Wind for France', 'The Far-Distant Oxus', 'A Glass of Blessings', 'A Time of Gifts',
    'The Skull Beneath the Skin', 'The Road Less Traveled', 'The Waste Land', 'Françoise Sagan', 'Antic Hay',
    'The Monkey''s Raincoat', 'No Longer at Ease', 'A Passage to India', 'Vanity Fair', 'O Jerusalem!',
    'No Country for Old Men', 'Carrion Comfort', 'An Acceptable Time', 'The Daffodil Sky', 'The Heart Is Deceitful Above All Things',
    'Little Hands Clapping', 'The Green Bay Tree', 'Consider the Lilies', 'Dulce et Decorum Est', 'For a Breath I Tarry',
    'Beneath the Bleeding', 'Tiger! Tiger!', 'The Glory and the Dream', 'Time To Murder And Create', 'The Parliament of Man',
    'Noli Me Tangere', 'When the Green Woods Laugh', 'The Way Through the Woods', 'A Swiftly Tilting Planet', 'Ring of Bright Water',
    'From Here to Eternity', 'The Torment of Others', 'All the King''s Men', 'To a God Unknown', 'Shall not Perish',
    'Eyeless in Gaza', 'Blood''s a Rover', 'The Soldier''s Art', 'The World, the Flesh and the Devil', 'The Mirror Crack''d from Side to Side',
    'Rosemary Sutcliff', 'Butter In a Lordly Dish', 'Sleep the Brave', 'The Cricket on the Hearth', 'A Time to Kill');
    v_authors := t_list('Gayle Hintz DDS', 'Darcy Weber', 'Magali Hayes', 'Donte Deckow', 'Lionel Morissette',
    'Rhiannon Breitenberg', 'Kaila Stehr DDS', 'Shawn Klein', 'Deanne Beier PhD', 'Jordon Adams',
    'Tanja Jakubowski', 'Miss Verlene Bergstrom', 'Tory Hettinger', 'Katharyn Pouros', 'Agueda Grimes',
    'Britta Hane', 'Richard Altenwerth PhD', 'Miss Blair Senger', 'Mrs. Leone Crooks', 'Gregory McKenzie',
    'Fermin Lakin II', 'Solomon Schuster', 'Kala Koss', 'Anissa Cartwright', 'Donny Schulist',
    'Delicia Hoppe', 'Laurice Kuphal', 'Modesto Hessel', 'Leon Satterfield', 'Mrs. Audry Lesch',
    'Miss Jolene Gislason', 'Lamont Volkman', 'Christian Luettgen', 'Thanh Wuckert', 'Janelle Leannon',
    'Gerardo Dibbert', 'Mr. Major Jast', 'Karmen Turcotte', 'Orpha Stamm', 'Lauren Feeney',
    'Jennell Yundt MD', 'Eusebia Hartmann', 'Louella Heathcote', 'Theron Schaefer', 'Mrs. Grover Effertz',
    'Jamison Muller MD', 'Eldridge Heathcote', 'Lino Koelpin', 'Delia Will', 'Samatha Hyatt');
    v_genres := t_list('Comic/Graphic Novel', 'Textbook', 'Humor', 'Fable', 'Speech',
    'Mystery', 'Fiction narrative', 'Horror', 'Metafiction', 'Short story',
    'Folklore', 'Fanfiction', 'Essay', 'Biography/Autobiography', 'Historical fiction',
    'Classic', 'Science fiction', 'Mythology', 'Mythopoeia', 'Fairy tale');
    SELECT COUNT(id) INTO v_students FROM studenti;
    FOR c_id_book IN 1 .. 400 LOOP
        v_id_student := DBMS_RANDOM.VALUE(1, v_students + 1);
        v_title := v_titles(TRUNC(DBMS_RANDOM.VALUE(1, v_titles.count + 1)));
        v_author := v_authors(TRUNC(DBMS_RANDOM.VALUE(1, v_authors.count +1)));
        v_genre := v_genres(TRUNC(DBMS_RANDOM.VALUE(1, v_genres.count + 1)));
        v_year := DBMS_RANDOM.VALUE(1800, EXTRACT(YEAR FROM SYSDATE) + 1);
        v_pages := DBMS_RANDOM.VALUE(100, 1001);
        INSERT INTO books VALUES (c_id_book, v_id_student, v_title, v_author, v_genre, v_year, v_pages);
    END LOOP;
END;
/
SELECT * FROM books ORDER BY id_student;