# Use the official Maven image to build the app
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package

# Use a lightweight JDK image to run the app
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# 👇 TEMPORARY: List jar contents for debugging
RUN jar tf app.jar | grep application.properties

ENTRYPOINT ["java", "-jar", "app.jar", "--debug"]
