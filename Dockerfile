# syntax=docker/dockerfile:1

# ---------- Stage 1: Build ----------
FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /build

# Копируем Maven wrapper и pom отдельно — для кеширования зависимостей
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Скачиваем зависимости (кешируется, если pom.xml не менялся)
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw dependency:go-offline -DskipTests

# Копируем исходники и собираем
COPY src src
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw clean package -DskipTests

# Переименовываем jar в app.jar для удобства
RUN mv target/*.jar target/app.jar

# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:21-jre-jammy AS runtime

# Создаём непривилегированного пользователя
RUN groupadd --system spring && useradd --system --gid spring spring

WORKDIR /app

# Копируем только собранный jar
COPY --from=build /build/target/app.jar app.jar

# Даём права пользователю
RUN chown spring:spring app.jar

USER spring:spring

EXPOSE 8080

# JVM настройки для контейнера
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "app.jar"]