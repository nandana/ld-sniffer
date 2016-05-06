#! /bin/sh
cd /home/nandana/tools/fuseki-2.3.1/
java -jar fuseki-server.jar --loc=/home/nandana/srcroot/gh-m/ld-sniffer/src/main/resources/database/test  /ldqm

/home/nandana/tools/apache-jena-2.12.1/bin --loc=/home/nandana/data/ldqm/dboperson > dbodump.nq