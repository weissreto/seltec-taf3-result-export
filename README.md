# seltec-taf3-result-export

## English

### Description

This utility reads a Seltec TAF 3 result export CSV file and produces Microsoft Excel files in different formats.

### Supported Output Formats

The following formats are supported:
* Resultate : Generates a Excel File with a tab for each class and event sorted by rank
* FinalSchnellsti : Generates a Excel File for each class and event sorted by rank. You can edit this file and reimport it to TAF3 using the provided TAF3 import template file (`templates\import.tim`).


### Usage

    SeltecTaf3Export Resultate seltec-result-exports.csv Resultate.xlsx
   
    SeltecTaf3Export FinalSchnellsti seltec-result-exports.csv final  

### Installation

1. Unzip the seltec-taf3-export-0.1.0-SNAPSHOT-installer.zip file into any directory.
 
### Build

1. Checkout the repository
2. Run `mvn clean install`
   
## Deutsch

### Beschreibung

Dieses Tool liest eine exportiere Seltec TAF 3 Resultatsdatei CSV und schreibt Microsoft Excel Dateien in verschiedenden Ausgabe Formaten.

### Unersützte Ausgabe Formate

Die folgenden Ausgabe Formate sind unterstützt:
* Resultate : Schreibt eine Excel Datei mit einem Tab für jede Kategorie und Bewerb sortiert nach Rang
* FinalSchnellsti : Schreibt eine Excel Datei für jede Kategorie und Bewerb sortiert nach Rang. Sie können die Datei bearbeiten und dann erneut in TAF 3 mit der beiliegenden Templatedatei (`templates\import.tim`) importieren.

### Usage

    SeltecTaf3Export Resultate seltec-result-exports.csv Resultate.xlsx
   
    SeltecTaf3Export FinalSchnellsti seltec-result-exports.csv final  

### Installation

1. Entpacken Sie die Datei seltec-taf3-export-0.1.0-SNAPSHOT-installer.zip in ein beliebiges Verzeichnis.
 
      
   