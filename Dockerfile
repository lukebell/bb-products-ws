FROM openjdk:17-alpine
LABEL app=products-ws
ADD target/product-ws-0.0.1-SNAPSHOT.jar $JAR_PATH
COPY src/main/resources/xsd /opt/app/xsd
CMD ["spring-start"]