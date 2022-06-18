FROM openjdk:8
WORKDIR /
ADD target/userService.jar //
EXPOSE 8083
ENTRYPOINT ["java","-jar","/userService.jar"]