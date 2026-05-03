# Use Java 17
FROM openjdk:17-jdk-slim

# Copy project
WORKDIR /app
COPY . .

# Build the project
RUN ./mvnw clean package -DskipTests

# Run the jar
CMD ["java", "-jar", "target/*.jar"]