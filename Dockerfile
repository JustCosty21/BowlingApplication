FROM anapsix/alpine-java
MAINTAINER costache

WORKDIR /app

COPY ./bowling/target/bowling-1.0-SNAPSHOT.jar .

CMD ["java","-jar","./bowling-1.0-SNAPSHOT.jar"]