FROM nexusinternal.gim.be/repository/docker-releases/docker-java/25-jre:latest
MAINTAINER Wim De Smet
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} backend-server.jar
ENTRYPOINT ["java", "-jar", "backend-server.jar"]