# ESTÁGIO 1: Compilação
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app


COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# ESTÁGIO 2: Execução
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Garante que a aplicação use o perfil correto e as variáveis do docker-compose
ENTRYPOINT ["java", "-Dspring.profiles.active=default", "-jar", "app.jar"]