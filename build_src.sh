#!/bin/sh

mvn clean install eclipse:eclipse -Pwar -Pdev -Pmysql -Dwtpversion=1.5 -DdownloadSources=true -DdownloadJavadocs=true
