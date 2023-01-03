FROM maven:3.8.7-eclipse-temurin-17-alpine as builder

COPY src /usr/src/app/src
COPY pom.xml /usr/src/app

RUN mvn -f /usr/src/app/pom.xml clean package

FROM eclipse-temurin:17-jre-alpine

COPY --from=builder /usr/src/app/target/*.jar /usr/app/app.jar

ENTRYPOINT ["java","-jar","/usr/app/app.jar"]
