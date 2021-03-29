FROM java:8
COPY ./build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar", "-Xmx300M","/app.jar"]
EXPOSE 8000