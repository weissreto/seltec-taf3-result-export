package ch.rweiss.seltec.taf3.export;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;

record Result(int rank, Athlete athlete, Competition competition, String value) {

  public static final Comparator<Result> RANK_COMPERATOR = Comparator.comparing(Result::rank);

  static final CSVFormat SELTEC_RESULT_EXPORT_FORMAT = CSVFormat.Builder.create()
      .setDelimiter(';')
      .setSkipHeaderRecord(true)
      .setHeader("Type", "Bib", "Code", "FirstName", "LastName", "Yob", "Gender", "RelayNumber", "RelayName", "Nation",
                 "ClubName", "ClubCode", "ClubNation", "Event", "EventInfo", "Class", "NotCompetitive", "Result", "Wind",
                 "HeatRank", "RoundRank", "HeatNr", "Lane", "SinglePoints", "Details")
      .build();

  /*
   * Format: Type;Bib;Code;FirstName;LastName;Yob;Gender;RelayNumber;RelayName;Nation;ClubName;ClubCode;ClubNation;Event;EventInfo;Class;NotCompetitive;Result;Wind;HeatRank;RoundRank;HeatNr;Lane;SinglePoints;Details
   */
  static List<Result> readFrom(Path resultsFile) throws IOException {
    try (var reader = Files.newBufferedReader(resultsFile, StandardCharsets.ISO_8859_1)) {
      var csvReader = SELTEC_RESULT_EXPORT_FORMAT.parse(reader);
      var records = csvReader.getRecords();
      return records
          .stream()
          .map(CsvRecord::new)
          .map(Result::fromRecord)
          .toList();
    }
  }

  static Result fromRecord(CsvRecord record) {
    return new Result(
        record.asOptionalInt("RoundRank"),
        Athlete.fromRecord(record),
        Competition.fromRecord(record),
        record.asString("Result"));
  }
}
