CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE blockchain
(
    id     UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    name   TEXT NOT NULL,
    symbol TEXT NOT NULL
);