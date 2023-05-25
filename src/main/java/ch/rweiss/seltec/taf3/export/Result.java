package ch.rweiss.seltec.taf3.export;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

record Result(int rank, Athlete athlete, Competition competition, String value) {

  public static final Comparator<Result> RANK_COMPERATOR = Comparator.comparing(Result::rank);

  private static final CSVFormat SELTEC_RESULT_EXPORT_FORMAT = CSVFormat.Builder.create()
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
      return records.stream().map(Result::fromRecord).toList();
    }
  }

  static Result fromRecord(CSVRecord record) {
    return new Result(
        toRank(record.get("RoundRank")),
        Athlete.fromRecord(record),
        Competition.fromRecord(record),
        record.get("Result"));
  }

  private static int toRank(String rank) {
    if (rank.isBlank()) {
      return Integer.MAX_VALUE;
    }
    return Integer.parseInt(rank);
  }

}
