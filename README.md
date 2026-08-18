# slzvieira-news

A simple REST microservice that serves news articles, built with Spring Boot as a personal/learning project.

## Tech stack

- Java 21
- Spring Boot 4.1 (Spring MVC)
- Spring Data Commons (pagination support)
- Lombok
- Maven

## API

Base path: `/news/v1`

| Method | Path                | Description                                  |
|--------|---------------------|----------------------------------------------|
| GET    | `/news/v1`          | List news, paginated (`page`, `size`, `sort`)|
| GET    | `/news/v1/{id}`     | Get a single news item by id                 |
| GET    | `/news/v1/random`   | Get a random news item                       |

### Examples

```bash
curl http://localhost:8080/news/v1?page=0&size=5
curl http://localhost:8080/news/v1/3
curl http://localhost:8080/news/v1/random
```

## News data

News items are loaded at startup from `src/main/resources/news.txt` by `NewsRepository`. Each item occupies 4 lines, separated by a blank line:

```
Title
Content
CATEGORY
DD/MM/YYYY
```

`CATEGORY` must match one of the values in `NewsCategory` (`SPORT`, `POLITICS`, `SCIENCE`, `MUSIC`).

## Build

Compile, run tests and package the application into a jar:

```bash
./mvnw clean package
```

## Docker image (Jib)

Jib builds the application into a Docker image without requiring a Dockerfile or a running Docker daemon (unless using `dockerBuild`). The image is based on `eclipse-temurin:21-jre`, configured in `pom.xml`.

Build the image and load it into your local Docker daemon:

```bash
./mvnw clean package jib:dockerBuild
```

Build the image and push it directly to a registry (no local Docker daemon required):

```bash
./mvnw clean package jib:build
```

`package` must run before either Jib goal because the plugin is configured with `containerizingMode=packaged`, which containerizes the built jar (`target/app.jar`) rather than the compiled classes — `compile` alone does not produce that jar.
