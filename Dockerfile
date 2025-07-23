# ---------- STAGE 1: Build ----------
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set work directory
WORKDIR /app

# Copy Maven files and download dependencies first (for layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the JAR file
RUN mvn clean package -DskipTests

# ---------- STAGE 2: Run ----------
FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Let Render know the app will run on this port
ENV PORT=8080

# Tell Spring Boot to bind to 0.0.0.0 to work on Render or Docker cloud
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8006
ENV SERVER_ADDRESS=0.0.0.0

# Create a non-root user for security (optional but recommended)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
