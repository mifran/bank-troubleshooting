CREATE TABLE account
(
    ID             BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME           CHARACTER(100),
    TYPE           CHARACTER(50)
);