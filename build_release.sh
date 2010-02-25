#!/bin/sh

rm -rf ./release

mkdir -p release/postgresql
mkdir -p release/mysql
mkdir -p release/standalone

mvn clean install assembly:assembly -Pprod -Pstandalone -Pderby  -Dmaven.test.skip
cp target/*.zip release/standalone

mvn clean package assembly:assembly -Ppostgresql -Pprod -Pwar -Dmaven.test.skip
cp target/*.zip release/postgresql

mvn clean install assembly:assembly -Pmysql -Pprod -Pwar -Dmaven.test.skip
cp target/*.zip release/mysql

cd release

for name in postgresql/*; 
do 
	echo $name \
	 	| tr '[:upper:]' '[:lower:]'  \
		| sed 's/.*\///' \
		| sed 's/war/war-postgresql/' \
		| xargs mv $name \
		;
done

for name in mysql/*; 
do 
	echo $name  \
	 	| tr '[:upper:]' '[:lower:]' \
		| sed 's/.*\///' \
		| sed 's/war/war-mysql/' \
		| xargs mv $name \
		;
done

for name in standalone/*; 
do 
	echo $name  \
	 	| tr '[:upper:]' '[:lower:]' \
		| sed 's/.*\///' \
		| xargs mv $name \
		;
done

rmdir standalone mysql postgresql

cd ..
