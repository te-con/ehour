#!/bin/sh

mkdir -p release/postgresql
mkdir -p release/mysql

#mvn clean install assembly:assembly -Pprod -Pstandalone -Pderby  -Dmaven.test.skip
#cp target/*.zip release/

mvn clean package assembly:assembly -Ppostgresql -Pprod -Pwara -Pwar -Dmaven.test.skip
cp target/*.zip release/postgresql

#mvn clean install assembly:assembly -Pmysql -Pprod -Pwar -Dmaven.test.skip
#cp target/*.zip release/mysql
