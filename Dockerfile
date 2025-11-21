# ----------- BUILD STAGE -------------
FROM gradle:8.4-jdk17 AS builder
WORKDIR /app

# Copy gradle files first (for caching)
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
COPY gradlew ./gradlew
COPY gradlew.bat ./gradlew.bat

# Copy entire project
COPY . .

# Build the JAR (no tests)
#RUN ./gradlew clean build -x test
RUN chmod +x gradlew
RUN ./gradlew clean build -x test



# ----------- RUNTIME STAGE -------------
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy the built jar from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Required for Render to assign port
ENV PORT=8080
EXPOSE 8080

# Start Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
