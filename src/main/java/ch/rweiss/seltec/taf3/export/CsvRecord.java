package ch.rweiss.seltec.taf3.export;

import org.apache.commons.csv.CSVRecord;

class CsvRecord {
  private final CSVRecord record;

  CsvRecord(CSVRecord record) {
    this.record = record;
  }

  String asString(String column) {
    return record.get(column);
  }

  int asInt(String column) {
    var value = asString(column);
    return toInt(column, value);
  }

  int asOptionalInt(String column) {
    var value = asString(column);
    if (value.isBlank()) {
      return Integer.MAX_VALUE;
    }
    return toInt(column, value);
  }

  private int toInt(String column, String value) {
    try {
      return Integer.parseInt(value);
    } catch(NumberFormatException ex) {
      System.err.println("Cannot parse value " + value + " of " + column + " of record " + record);
      return Integer.MAX_VALUE;
    }
  }
}
