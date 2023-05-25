package ch.rweiss.seltec.taf3.export;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVRecord;

record Competition(String discipline, String category) {
  static Set<Competition> fromResults(List<Result> results) {
    return results
        .stream()
        .map(Result::competition)
        .collect(Collectors.toSet());
  }

  public static Competition fromRecord(CSVRecord record) {
    return new Competition(record.get("Event"), record.get("Class"));
  }
}
