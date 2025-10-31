DROP TABLE IF EXISTS currency CASCADE;

DROP SEQUENCE IF EXISTS create_sequence;
CREATE SEQUENCE create_sequence START 1 INCREMENT 1;

CREATE TABLE currency (
                          id BIGINT PRIMARY KEY DEFAULT nextval('create_sequence'),
                          name VARCHAR(255) NOT NULL,
                          nominal BIGINT NOT NULL,
                          value DOUBLE PRECISION NOT NULL,
                          iso_num_code BIGINT,
                          iso_code VARCHAR(3) UNIQUE
);