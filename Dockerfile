FROM eclipse-temurin:17-jdk-alpine
COPY ./build/libs/*SNAPSHOP.jar project.jar
ENTRYPOINT ["java", "-jar", "project.jar"]