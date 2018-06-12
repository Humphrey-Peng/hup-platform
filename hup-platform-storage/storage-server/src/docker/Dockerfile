FROM java:8
VOLUME /tmp
ADD storage-server-1.0-SNAPSHOT.jar storage-server.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/storage-server.jar"]
EXPOSE 8761
