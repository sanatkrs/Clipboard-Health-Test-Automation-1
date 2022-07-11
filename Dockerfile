FROM openjdk:18
ADD target/clipboard-health-docker-project.jar clipboard-health-docker-project.jar
ENTRYPOINT ["java", "-jar","clipboard-health-docker-project.jar"]
EXPOSE 8080