#!/bin/sh

mvn clover2:setup test clover2:aggregate clover2:clover -Pclover -Pwar -Pdev -Pmysql
