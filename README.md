# My Arceus Tracker

A simple and interactive tracker for **Pokémon Legends: Arceus**. This application helps players keep track of their progress in the Hisui Pokedex, complete research tasks, and monitor location-based encounters.

## Features

- **Hisui Pokedex**: View a comprehensive list of all Pokémon available in the Hisui region.
- **Research Tasks (ToDos)**: Track specific research tasks for each Pokémon to complete their Pokedex entry.
- **Advanced Filtering**: Filter the Pokémon list by:
  - **Name**: Search for specific Pokémon.
  - **Number**: Find Pokémon by their Hisui Pokedex number.
  - **Area**: Filter by specific regions (e.g., Obsidian Fieldlands, Crimson Mirelands).
- **Pagination**: Browse the Pokedex efficiently with a paginated list view.
- **Responsive Design**: Clean and functional UI built with Kotlin HTML DSL.

## Tech Stack

This project is built using modern Kotlin technologies:

- **Language**: [Kotlin](https://kotlinlang.org/)
- **Web Framework**: [Ktor](https://ktor.io/)
- **Database Access**: [Exposed](https://github.com/JetBrains/Exposed) (ORM)
- **Database**: [PostgreSQL](https://www.postgresql.org/) (Production) / [H2](https://www.h2database.com/html/main.html) (Testing)
- **Frontend**: Server-Side Rendering with [Kotlin HTML DSL](https://github.com/Kotlin/kotlinx.html)
- **Build Tool**: [Gradle](https://gradle.org/)
- **AI Assistant**: [Antigravity](https://gemini.google.com/app/antigravity)

## Getting Started

### Prerequisites

- JDK 17 or higher
- Docker (optional, for running with PostgreSQL)

### Running the Application

This project can be run using **[Docker Compose](https://docs.docker.com/compose)** (recommended) or locally.

#### Option 1: Run with Docker Compose (recommended)

The project includes a `docker-compose.yml` file that starts:
- A PostgreSQL database
- The application container

First, build and start the containers:

```bash
docker-compose build
docker-compose up
```

#### Option 2: Run locally without Docker

If you prefer running the app locally, make sure you have a PostgreSQL instance running and properly configured.

Then run:

```bash
./gradlew run
```

The server will start at http://0.0.0.0:8080.

### Running Tests

To execute the unit and integration tests:

```bash
./gradlew test
```

## Project Structure

- `src/main/kotlin`: Application source code.
  - `models`: Data classes and domain logic.
  - `database`: Database tables and DAO implementation.
  - `views`: UI components and page templates (HTML DSL).
  - `controllers`: Business logic handling.
- `src/test/kotlin`: Unit and integration tests.

## Resources

- **Pokémon Icons**: [HybridShivam/Pokemon](https://github.com/HybridShivam/Pokemon)
- **Pokémon type Icons**: [partywhale/pokemon-type-icons](https://github.com/partywhale/pokemon-type-icons)
- **PokeAPI**: https://pokeapi.co/. External API to retrieve additional Pokémon data that is not stored locally, such as evolution chains.
