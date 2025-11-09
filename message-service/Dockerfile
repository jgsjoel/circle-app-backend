# Step 1: Use an official JDK to build the project
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Step 2: Copy Maven files first to leverage Docker cache
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Step 3: Download dependencies (so they are cached)
RUN ./mvnw dependency:go-offline

# Step 4: Copy the actual project files
COPY src src

# Step 5: Build the Spring Boot jar
RUN ./mvnw clean package -DskipTests

# Step 6: Use a smaller JDK image to run the app
FROM eclipse-temurin:21-jre
WORKDIR /app

# Step 7: Copy only the jar from the builder stage
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]
