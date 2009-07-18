#!/bin/sh

mkdir -p release

mvn clean package assembly:assembly -Denv=prod-derby -Dmaven.test.skip
cp target/*.zip release/

mvn clean package assembly:assembly -Denv=prod-mysql -Dmaven.test.skip
cp target/*.zip release/

