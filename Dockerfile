FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

RUN apk add --no-cache maven

COPY pom.xml .
COPY src ./src

RUN mvn clean package
EXPOSE 8080
CMD ["mvn", "spring-boot:run"]