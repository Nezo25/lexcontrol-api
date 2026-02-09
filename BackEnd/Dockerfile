# ESTÁGIO 1: Compilação
FROM debian:bookworm-slim AS build
RUN apt-get update && apt-get install -y openjdk-21-jdk maven curl
WORKDIR /app

# Nota: Como o Java 25 é muito novo, compilamos com 21
# mas garantimos que o ambiente do container seja limpo.
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# ESTÁGIO 2: Execução
FROM debian:bookworm-slim
RUN apt-get update && apt-get install -y openjdk-21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]