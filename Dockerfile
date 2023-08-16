FROM openjdk:8

EXPOSE 8094

COPY target/*.jar /

ENTRYPOINT ["java","-jar","book-postgres-0.0.1-SNAPSHOT.jar"]