Linked Data Sniffer
===================
[![Build Status](https://travis-ci.org/ldp4j/ldp4j.svg?branch=master)](https://travis-ci.org/ldp4j/ldp4j)
[![GitHub license](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://raw.githubusercontent.com/nandana/ld-sniffer/master/LICENSE)
[![GitHub issues](https://img.shields.io/github/issues/nandana/ld-sniffer.svg)](https://github.com/nandana/ld-sniffer/issues)


Linked Data Sniffer is a command line tool for assessing the accessibility of Linked Data resources according to
the [metrics defined](http://delicias.dia.fi.upm.es/LDQM/index.php/Accessibility) in the
[Linked Data Quality Model](http://www.linkeddata.es/ontology/ldq#).


## Usage

Download the ld-sniffer-0.0.1.jar and use it as an standalone executable jar.

  usage: LDSnifferApp [-h] -tdb <TDB-DIR-PATH> -ul <URI-FILE-PATH>
  Assess a list of Linked Data resources using Linked Data Quality Model.
   -h,--help                       Print this help message
   -tdb,--tdb <TDB-DIR-PATH>       The path of directory for Jena TDB files
   -ul,--uriList <URI-FILE-PATH>   The path of the file containing the urls of resources to  be assessed
   Please report issues at https://github.com/nandana/ld-sniffer




