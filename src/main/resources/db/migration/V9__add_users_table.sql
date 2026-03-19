CREATE TABLE users {
    id SERIAL   PRIMARY KEY,
    email       VARCHAR(50) NOT NULL,
    password    VARCHAR(50) NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP NOT NULL DEFAULT NOW()
}

CREATE TABLE user_todos {
    id SERIAL   PRIMARY KEY,
    user_id     INTEGER NOT NULL REFERENCES users(id),
    pokemon_id  INTEGER NOT NULL REFERENCES pokemons(id),
    todo_id     INTEGER NOT NULL REFERENCES todos(id),
    done        INTEGER NOT NULL,
    completed   BOOLEAN NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP NOT NULL DEFAULT NOW()
}
