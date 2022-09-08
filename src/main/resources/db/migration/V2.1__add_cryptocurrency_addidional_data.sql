CREATE TABLE cryptocurrency_data
(
    cryptocurrency_id UUID NOT NULL PRIMARY KEY,
    creation_date     date
);

ALTER TABLE cryptocurrency_data
    ADD CONSTRAINT FK_CRYPTOCURRENCY_DATA_ON_CRYPTOCURRENCY FOREIGN KEY (cryptocurrency_id) REFERENCES cryptocurrency (id);