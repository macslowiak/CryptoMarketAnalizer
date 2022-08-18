ALTER TABLE blockchain
    ADD CONSTRAINT uc_blockchainuniquenameandsymbol UNIQUE (name, symbol);

ALTER TABLE cryptocurrency
    ADD CONSTRAINT uc_cryptocurrencyuniquenameandsymbolandaddress UNIQUE (name, symbol, address);