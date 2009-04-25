#!/bin/sh

mkdir release

mvn clean package assembly:assembly -Denv=prod-derby
cp target/*.zip release/

mvn clean package assembly:assembly -Denv=prod-mysql
cp target/*.zip release/

