CREATE TABLE pokemons
(
    id        INTEGER     NOT NULL,
    name      VARCHAR(20) NOT NULL,
    types     INTEGER[] NOT NULL DEFAULT '{}',
    locations INTEGER[] NOT NULL DEFAULT '{}',
    to_dos    JSONB       NOT NULL DEFAULT '{}',
    CONSTRAINT pk_pokemons PRIMARY KEY (id)
);

CREATE TABLE to_dos
(
    id          INTEGER     NOT NULL,
    description VARCHAR(70) NOT NULL,
    CONSTRAINT pk_todos PRIMARY KEY (id)
);

CREATE TABLE types
(
    id   INTEGER     NOT NULL,
    name VARCHAR(20) NOT NULL,
    CONSTRAINT pk_types PRIMARY KEY (id)
);

INSERT INTO types (id, name)
VALUES (0, 'BUG'),
       (1, 'DARK'),
       (2, 'DRAGON'),
       (3, 'ELECTRIC'),
       (4, 'FAIRY'),
       (5, 'FIGHTING'),
       (6, 'FIRE'),
       (7, 'FLYING'),
       (8, 'GHOST'),
       (9, 'GRASS'),
       (10, 'GROUND'),
       (11, 'ICE'),
       (12, 'NORMAL'),
       (13, 'POISON'),
       (14, 'PSYCHIC'),
       (15, 'ROCK'),
       (16, 'STEEL'),
       (17, 'WATER');
