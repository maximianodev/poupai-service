FROM openjdk:17-jdk-slim

WORKDIR /app

COPY pom.xml .

RUN apt-get update && apt-get install -y maven
RUN mvn dependency:go-offline -Dmaven.multiModuleProjectDirectory=.

COPY src ./src

RUN mvn clean package -DskipTests

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]