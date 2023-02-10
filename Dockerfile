FROM openjdk:18
EXPOSE 8080
ADD /target/aggregatorservice-1.0.0.jar aggregatorservice-1.0.0.jar
ENTRYPOINT ["java","-jar","aggregatorservice-1.0.0.jar"]
