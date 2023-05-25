package ch.rweiss.seltec.taf3.export;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;

public class SeltecTaf3Export {

  private final Path resultsFilePath;
  private final Path outputPath;

  public SeltecTaf3Export(String resultFilePath, String outputPath) {
    this.resultsFilePath = Path.of(resultFilePath);
    this.outputPath = Path.of(outputPath);
  }

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("Please provide an exported result *.csv file from Seltec TAF 3");
      System.exit(1);
    }
    try {
      new SeltecTaf3Export(args[0], System.getProperty("user.dir")).generateResults();
    }
    catch(Exception ex) {
      System.err.println("Could not generate per category and discipline result *.csv files");
      System.err.println("Reason:");
      ex.printStackTrace();
      System.exit(2);
    }
    System.exit(0);
  }

  void generateResults() throws IOException {
    var results = Result.readFrom(resultsFilePath);
    var competitions = Competition.fromResults(results);
    for (var competition : competitions) {
      writeResults(competition, results);
    }
  }

  private void writeResults(Competition competition, List<Result> results) throws IOException {
    results = new ArrayList<>(results
        .stream()
        .filter(result -> result.competition().equals(competition))
        .toList());
    Collections.sort(results, Result.RANK_COMPERATOR);
    var resultFilePath = outputPath.resolve(competition.discipline()+"-"+competition.category()+"-results.csv");
    try (var writer = Files.newBufferedWriter(resultFilePath, StandardCharsets.ISO_8859_1)) {
      System.out.println("Writing "+resultFilePath);
      var printer = CSVFormat.EXCEL.print(writer);
      printer.printRecord("Rank", "StartNumber", "LastName", "FirstName", "Yob", "Nation", "ClubName", "Result");
      for (var result : results) {
        printer.printRecord(
            result.rank(),
            result.athlete().startNumber(),
            result.athlete().lastName(),
            result.athlete().firstName(),
            result.athlete().yearOfBirth(),
            result.athlete().nation(),
            result.athlete().clubName(),
            result.value());
      }
    }
  }
}
