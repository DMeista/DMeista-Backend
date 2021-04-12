FROM java:8
VOLUME /tmp
EXPOSE 8000
COPY ./build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar", "-Xmx300M","/app.jar"]
