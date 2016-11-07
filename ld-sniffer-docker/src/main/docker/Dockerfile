FROM frolvlad/alpine-oraclejdk8:slim
MAINTAINER Nandana Mihindukulasooriya <https://twitter.com/nandanamihindu>
VOLUME /tmp
ADD ld-sniffer-webapp-0.0.1.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]