#!/bin/sh

LINE=`grep -n ^package $1 | sed 's/\:.*//'`
LINE=`expr $LINE - 1`
sed "1,${LINE}d" $1 > temp.txt
cat license.txt temp.txt > joined.txt
mv joined.txt ${1}

