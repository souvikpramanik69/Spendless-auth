# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
# Cache dependencies
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Package stage
FROM eclipse-temurin:21-jre
COPY --from=build /app/target/*.jar /app.jar
EXPOSE 8006
ENTRYPOINT ["java", "-jar", "/app.jar"]