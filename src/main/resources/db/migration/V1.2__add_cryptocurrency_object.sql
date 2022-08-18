CREATE TABLE cryptocurrency
(
    id            UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    name          TEXT NOT NULL,
    symbol        TEXT NOT NULL,
    blockchain_id UUID,
    address       TEXT
);

ALTER TABLE cryptocurrency
    ADD CONSTRAINT FK_CRYPTOCURRENCY_ON_BLOCKCHAIN FOREIGN KEY (blockchain_id) REFERENCES blockchain (id);
