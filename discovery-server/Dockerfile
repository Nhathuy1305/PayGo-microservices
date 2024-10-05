FROM eclipse-temurin:17-jdk-focal AS build

WORKDIR /build

COPY gradle/ ./gradle
COPY build.gradle settings.gradle gradlew ./
RUN sed -i 's/\r$//' gradlew
RUN ./gradlew build --no-daemon

COPY . .
RUN sed -i 's/\r$//' gradlew
RUN ./gradlew build --no-daemon -x test

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /build/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]