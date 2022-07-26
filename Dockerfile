FROM openjdk:16

LABEL owner="rishith"

ADD target/todolist-0.0.1-SNAPSHOT.jar todolist.jar


EXPOSE 8080
ENTRYPOINT ["java","-jar", "todolist.jar"]
