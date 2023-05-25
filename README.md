# seltec-taf3-result-export

## Description
This utility reads a Seltec TAF 3 result export *.csv file and produces *-results.csv files for each discipline and category ordered by rang.
Output files are generated to the current directory.

## Usage

    SeltecTaf3Export seltec-result-exports.csv

## Installation

1. Unzip seltec-taf3-export-0.0.1-SNAPSHOT-installer.zip file.
2. Either:
    1. Unzip a Java 17 JRE into the jre directory in the installer directory
    2. Set JAVA_HOME to a Java 17 JRE installation path.
 
## Build

1. Checkout the repository
2. Run `mvn clean install`
   