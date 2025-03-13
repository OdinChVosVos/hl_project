FROM gradle:8.5-jdk21-alpine AS builder

RUN addgroup coomongroup; adduser --ingroup coomongroup --disabled-password commonuser

WORKDIR /app

COPY build.gradle settings.gradle ./

COPY gradlew gradle/ ./gradle/

RUN gradle --no-daemon dependencies

COPY . ./

RUN gradle bootJar --parallel --no-daemon

FROM eclipse-temurin:21-jre-alpine AS production

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar
COPY --from=builder /app/src/main/resources/*.yaml ./resources/
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
