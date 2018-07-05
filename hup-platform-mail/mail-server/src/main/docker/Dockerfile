FROM java:8
VOLUME /tmp
ADD mail-server-1.0-SNAPSHOT.jar mail-server.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/mail-server.jar"]
EXPOSE 8761
