FROM amazoncorretto:17-alpine-jdk
LABEL app=products-ws
ARG JAR_FILE=target/product-ws-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]