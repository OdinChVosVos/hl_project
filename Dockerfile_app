FROM gradle:8.5-jdk21-alpine AS builder

RUN addgroup -S coomongroup && adduser -S -G coomongroup -D commonuser

USER commonuser

WORKDIR /app

COPY --chown=commonuser:coomongroup build.gradle settings.gradle ./
COPY --chown=commonuser:coomongroup gradlew gradle/ ./gradle/

RUN gradle --no-daemon dependencies

COPY --chown=commonuser:coomongroup . ./

RUN gradle bootJar --parallel --no-daemon


FROM eclipse-temurin:21-jre-alpine AS production

RUN addgroup -S appgroup && adduser -S -G appgroup -D appuser

WORKDIR /app

COPY --from=builder --chown=appuser:appgroup /app/build/libs/*.jar /app/app.jar
COPY --from=builder --chown=appuser:appgroup /app/src/main/resources/*.yaml /app/resources/

USER appuser

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]