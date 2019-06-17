FROM java:8
VOLUME /tmp
ADD config-server-1.0-SNAPSHOT.jar config-server.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/config-server.jar"]
EXPOSE 8761
