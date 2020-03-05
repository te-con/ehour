FROM maven:3-jdk-8-alpine as builder
VOLUME /root/.m2
COPY . /source
WORKDIR /source
RUN cp -r repository /root/.m2/ && \
  mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file -Dfile=repository/nl/tecon/scalahighcharts/highcharts/1.7/highcharts-1.7.jar && \
  mvn install -Pprod  -Dmaven.test.skip=true -DskipTests

FROM openjdk:8
COPY --from=0 /source/eHour-standalone/target/ehour-1.4.4-SNAPSHOT-standalone/ehour-1.4.4-SNAPSHOT/ /ehour
WORKDIR /ehour
ENV EHOUR_HOME=/ehour
CMD java -cp "lib/*" net.rrm.ehour.EhourServerRunner
