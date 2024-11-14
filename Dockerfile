FROM maven:3.9.8-eclipse-temurin-21 AS build
RUN mkdir /opt/app
COPY . /opt/app
WORKDIR /opt/app
RUN mvn clean package
FROM eclipse-temurin:21-jre-alpine
RUN mkdir /opt/app
COPY --from=build  /opt/app/target/incident-manager-0.0.1-SNAPSHOT.jar /opt/app/app.jar
WORKDIR /opt/app
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]