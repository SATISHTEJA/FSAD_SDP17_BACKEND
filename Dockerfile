# Use official Eclipse Temurin (recommended)
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy everything
COPY . .

# Build using Maven Wrapper
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Run the app
CMD ["java", "-jar", "target/*.jar"]