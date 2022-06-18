FROM openjdk:8
WORKDIR /
ADD target/transaction.jar //
EXPOSE 8082
ENTRYPOINT ["java","-jar","/transaction.jar"]