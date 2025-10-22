# Builder stage
FROM maven:3.8.6-eclipse-temurin-17 AS builder

# Set build arguments for Maven optimization
ARG BUILD_CACHE=/root/.m2

WORKDIR /app

# Copy pom.xml first to leverage Docker cache
COPY pom.xml .
# Download dependencies (cached unless pom.xml changes)
RUN mvn -B dependency:go-offline

# Copy source code and build application
COPY src ./src
RUN mvn -B package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-jammy

# Set JVM options for production
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Create non-root user for security
RUN groupadd -r appuser && useradd -r -g appuser appuser

WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder --chown=appuser:appuser /app/target/*.jar app.jar

# Switch to non-root user
USER appuser

EXPOSE 8080

#ENTRYPOINT ["java", "-jar", "app.jar"]

# with JVM options:
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]