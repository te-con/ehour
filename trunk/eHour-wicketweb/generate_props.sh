#!/bin/sh

cd src/main/java
find . -name *.properties -print | grep -v _nl | xargs cat > ../../../allproperties.txt
cd -
