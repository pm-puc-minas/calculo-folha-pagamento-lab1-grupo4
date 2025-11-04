# Etapa 1: build da aplicação
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: imagem final com Java
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/FolhaPag-0.0.1-SNAPSHOT.jar app.jar

# Configura o perfil e porta
ENV SPRING_PROFILES_ACTIVE=dev
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
