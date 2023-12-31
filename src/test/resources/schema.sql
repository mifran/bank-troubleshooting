CREATE TABLE account
(
    ID             BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME           CHARACTER(100),
    TYPE           CHARACTER(50),
    OWNER          VARCHAR(256) NOT NULL
);

CREATE TABLE journal_entry
(
    ID             BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    ACCOUNT        BIGINT NOT NULL,
    DESCRIPTION    CHARACTER(100),
    INSTANT        BIGINT,
    AMOUNT         CHARACTER(20),
    BALANCE        CHARACTER(20)
)