FROM gradle:8.7-jdk17 AS build

WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .
RUN gradle installDist --no-daemon

FROM eclipse-temurin:17-jre-jammy

RUN apt-get update \
    && apt-get install --no-install-recommends -y \
        curl \
        tesseract-ocr \
        tesseract-ocr-eng \
        tesseract-ocr-spa \
    && rm -rf /var/lib/apt/lists/*

RUN useradd --create-home --uid 10001 app
WORKDIR /app
COPY --from=build --chown=app:app /home/gradle/project/build/install/my-arceus/ ./

USER app
EXPOSE 8080

ENTRYPOINT ["/app/bin/my-arceus"]
