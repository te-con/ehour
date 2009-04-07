#!/bin/sh

LINE=`grep -n ^package $1 | sed 's/\:.*//'`

if [ $LINE > 0 ]
then
	LINE=`expr $LINE - 1`
	sed "1,${LINE}d" $1 > temp.txt
else
	cp $1 temp.txt
fi

cat etc/license_chg/license.txt temp.txt > joined.txt
mv joined.txt ${1}
rm temp.txt

echo $1
