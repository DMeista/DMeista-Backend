FROM openjdk:12-alpine
EXPOSE 8000
COPY ./build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar", "-Xmx300M","/app.jar"]
