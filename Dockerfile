FROM eclipse-temurin:17-jdk-alpline
COPY ./build/libs/*SNAPSHOP.jar project.jar
ENTRYPOINT ["java", "-jar", "project.jar"]