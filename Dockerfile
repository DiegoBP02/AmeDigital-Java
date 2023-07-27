FROM openjdk:17
VOLUME /tmp
EXPOSE 8080
COPY target/demo-0.0.1-SNAPSHOT.jar ame-digital.jar
ENTRYPOINT ["java","-jar","/ame-digital.jar"]
