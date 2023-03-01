CREATE SCHEMA IF NOT EXISTS test;
CREATE TABLE test.account
(
    id        UUID NOT NULL,
    email     VARCHAR(255),
    full_name VARCHAR(255),
    password  VARCHAR(255),
    role      VARCHAR(255),
    username  VARCHAR(255),
    deleted   BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT account_pkey PRIMARY KEY (id)
);

CREATE TABLE test.pet
(
    id         UUID NOT NULL,
    name       VARCHAR(255),
    account_id UUID,
    deleted    BOOLEAN DEFAULT false,
    type       VARCHAR(255),
    CONSTRAINT pet_pkey PRIMARY KEY (id)
);

ALTER TABLE test.pet
    ADD CONSTRAINT fk2nrjk6qfkfqykera7g4rkq9uq FOREIGN KEY (account_id) REFERENCES account (id) ON UPDATE NO ACTION ON DELETE NO ACTION;