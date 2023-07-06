#
# Build stage
#
FROM maven:3.8.2-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM openjdk:17-jdk-slim
COPY --from=build /target/java-ecommerce-0.0.1-SNAPSHOT.jar java-ecommerce.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","java-ecommerce.jar"]
