CREATE TABLE pokemons
(
    id        INTEGER     NOT NULL,
    name      VARCHAR(20) NOT NULL,
    locations INTEGER[] NOT NULL DEFAULT '{}',
    to_dos    INTEGER[] NOT NULL DEFAULT '{}',
    CONSTRAINT pk_pokemons PRIMARY KEY (id)
);

CREATE TABLE toDos
(
    id          INTEGER NOT NULL,
    description VARCHAR NOT NULL,
    CONSTRAINT pk_todos PRIMARY KEY (id)
);
