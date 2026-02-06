# ESTÁGIO 1: Compilação (Build)
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copia os arquivos de configuração e o código fonte
COPY pom.xml .
COPY src ./src

# Compila o projeto e gera o arquivo .jar
RUN mvn clean package -DskipTests

# ESTÁGIO 2: Execução (Run)
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Busca o JAR gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]