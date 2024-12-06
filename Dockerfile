FROM eclipse-temurin:17 AS build

WORKDIR /app

COPY . .

RUN chmod +x mvnw

RUN --mount=type=secret,id=maven_settings,target=/root/.m2/settings.xml \
    ./mvnw clean package -DskipTests

FROM eclipse-temurin:23-jre-alpine

WORKDIR /app

COPY --from=build app/target/*.jar /app/auth-service.jar

EXPOSE 8080

CMD ["java", "-jar", "auth-service.jar"]