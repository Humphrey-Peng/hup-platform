FROM java:8
VOLUME /tmp
ADD rabbitmq-server-1.0-SNAPSHOT.jar rabbitmq-server.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/rabbitmq-server.jar"]
EXPOSE 8761
