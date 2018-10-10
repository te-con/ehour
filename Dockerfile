FROM maven:3-jdk-7-alpine
COPY . /source
WORKDIR /source
RUN mvn  clean install -Pprod  -Dmaven.test.skip=true -DskipTests