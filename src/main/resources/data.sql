MERGE INTO GENRE (GENRE_ID, GENRE_NAME)
    VALUES (1, 'Комедия'), (2, 'Драма'), (3, 'Мультфильм'), (4, 'Триллер'), (5, 'Документальный'), (6, 'Боевик');

MERGE INTO RATING (RATING_ID, NAME_RATING)
    VALUES (1, 'G'), (2, 'PG'), (3, 'PG-13'), (4, 'R'), (5, 'NC-17');

MERGE INTO STATUS (STATUS_ID, NAME_STATUS)
    VALUES(0,'CONFIRMED'),(1,'UNCONFIRMED')