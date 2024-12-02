FROM eclipse-temurin:17 AS build

WORKDIR /app

# Instalar herramientas necesarias
RUN apt-get update && \
    apt-get install -y bash curl && \
    rm -rf /var/lib/apt/lists/*

COPY . /app

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build app/target/*.jar /app/auth-service.jar

EXPOSE 8080

CMD ["java", "-jar", "auth-service.jar"]