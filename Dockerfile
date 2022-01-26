FROM openjdk:11-jre-slim

EXPOSE 8080

COPY *.jar app.jar

ENTRYPOINT ["java","-jar", "-Xmx300M","/app.jar", "-Duser.timezone=Asia/Seoul"]
