# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the applicationFROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar api-gateway.jar
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]
