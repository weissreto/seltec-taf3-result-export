package ch.rweiss.seltec.taf3.export;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

record Competition(String discipline, String category) {
  static Set<Competition> fromResults(List<Result> results) {
    return results
        .stream()
        .map(Result::competition)
        .collect(Collectors.toSet());
  }

  public static Competition fromRecord(CsvRecord record) {
    return new Competition(record.asString("Event"), record.asString("Class"));
  }
}
