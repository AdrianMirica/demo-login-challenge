FROM openjdk:17-alpine

WORKDIR /app

COPY target/demo-login-*.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]