Linked Data Sniffer
===================
[![Build Status](https://travis-ci.org/ldp4j/ldp4j.svg?branch=master)](https://travis-ci.org/ldp4j/ldp4j)
[![GitHub license](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://raw.githubusercontent.com/nandana/ld-sniffer/master/LICENSE)
[![Java version](https://img.shields.io/badge/java-1.8-green.svg)](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
[![GitHub issues](https://img.shields.io/github/issues/nandana/ld-sniffer.svg)](https://github.com/nandana/ld-sniffer/issues)
[![Maintenance](https://img.shields.io/maintenance/yes/2017.svg?maxAge=2592000?style=plastic)](https://twitter.com/nandanamihindu)


Linked Data Sniffer is a tool for assessing the accessibility of Linked Data resources according to
the [metrics defined](http://delicias.dia.fi.upm.es/LDQM/index.php/Accessibility) in the
[Linked Data Quality Model](http://www.linkeddata.es/ontology/ldq#).

Linked Data Sniffer can be used either as a Web App or a command line tool. 

## LD Sniffer Web App

![Web App Screenshot](https://github.com/nandana/ld-sniffer/blob/master/distribution/src/main/resources/ldsniffer.png)

Thanks [Idafen](https://github.com/idafensp) for the logo! 

## LD Sniffer Docker Image

The LD Sniffer Web App is also available as a Docker image [(nandana/ld-sniffer)](https://hub.docker.com/r/nandana/ld-sniffer/) in Docker hub. 

### Usage

$ docker run --name ld-sniffer -p 8080:8080 -it  nandana/ld-sniffer:0.0.1

You should see the web app getting started like the following and it will be available in the URL : http://localhost:8080/

![Docker Commandline](https://github.com/nandana/ld-sniffer/blob/master/distribution/src/main/resources/ld-sniffer-docker.png)

## LD Sniffer Command Line App

### Usage

Download the [ld-sniffer-0.0.1.jar](https://github.com/nandana/ld-sniffer/releases/tag/0.0.1) and use it as an standalone executable jar. You will need Java 8 as a prerequesite.
```
usage: java -jar ld-sniffer-0.0.1.jar [-h] [-md] [-ml <METRICS-FILE-PATH>] [-rdf] [-t <T-MINS>] [-tdb <TDB-DIR-PATH>] [-ul <URI-FILE-PATH>] -url <URL>
Assess a list of Linked Data resources using Linked Data Quality Model.
 -h,--help                               Print this help message
 -md,--metrics-definition                Include the metric definitions in the results
 -ml,--metricsList <METRICS-FILE-PATH>   The path of the file containing the list of metrics to be calculated
 -rdf,--rdf-output                       Output the RDF serialization of the results
 -t,--timeout <T-MINS>                   Timeout (in minutes) for a single evaluation
 -tdb,--tdb <TDB-DIR-PATH>               The path of directory for Jena TDB files
 -ul,--uriList <URI-FILE-PATH>           The path of the file containing the urls of resources to be assessed
 -url,--url <URL>                        URL of the resource to be assessed
Please report issues at https://github.com/nandana/ld-sniffer
```

## Evaluation results

* [Results of accessibility assessment of DBpedia resources](https://datahub.io/dataset/ldqm-dbpedia-2016)
* [Analysis of accessibility assessment of DBpedia resources](http://nandana.github.io/ld-sniffer/)
