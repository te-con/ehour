FROM maven:3-jdk-7-alpine as builder
VOLUME /root/.m2
COPY . /source
WORKDIR /source
RUN mvn  clean install -Pprod  -Dmaven.test.skip=true -DskipTests

FROM openjdk:7
COPY --from=0 /source/eHour-standalone/target/ehour-1.4.4-SNAPSHOT-standalone/ehour-1.4.4-SNAPSHOT/ /ehour
WORKDIR /ehour
ENV EHOUR_HOME=/ehour
CMD java -cp "lib/*" net.rrm.ehour.EhourServerRunner