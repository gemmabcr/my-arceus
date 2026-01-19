CREATE TABLE special_conditions
(
    id   INTEGER     NOT NULL,
    name VARCHAR(20) NOT NULL,
    CONSTRAINT pk_special_conditions PRIMARY KEY (id)
);

INSERT INTO special_conditions (id, name)
VALUES (0, 'DAY_TIME'),
       (1, 'NIGHT_TIME'),
       (2, 'NOT_FOGGY_WEATHER');

ALTER TABLE pokemons ADD COLUMN special_condition INTEGER;

UPDATE pokemons
SET special_condition = 0
WHERE id IN (12, 13, 19, 20, 75);

UPDATE pokemons
SET special_condition = 1
WHERE id IN (21, 22, 34, 35, 37, 38);

UPDATE pokemons
SET special_condition = 2
WHERE id IN (23, 24);
