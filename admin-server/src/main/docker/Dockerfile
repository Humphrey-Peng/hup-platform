FROM java:8
VOLUME /tmp
ADD admin-server-1.0-SNAPSHOT.jar admin-server.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/registry-server.jar"]
EXPOSE 8761
