# Use Java 17 base image
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy and build with Maven
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve

COPY src ./src

RUN ./mvnw clean install -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/school-management-system-1.0.jar"]
