#!/bin/sh

mkdir -p release

mvn clean install assembly:assembly -Denv=prod-derby -Dmaven.test.skip
cp target/*.zip release/

mvn clean install assembly:assembly -Denv=prod-mysql -Dmaven.test.skip
cp target/*.zip release/

