CREATE TABLE user_team
(
    id         SERIAL PRIMARY KEY,
    user_id    INTEGER   NOT NULL REFERENCES users (id),
    pokemon_id INTEGER   NOT NULL REFERENCES pokemons (id),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT user_team_unique UNIQUE (user_id, pokemon_id)
);
