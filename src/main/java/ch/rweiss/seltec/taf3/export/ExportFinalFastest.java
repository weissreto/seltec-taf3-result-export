package ch.rweiss.seltec.taf3.export;

import java.io.IOException;
import java.util.List;
import ch.rweiss.seltec.taf3.export.excel.Excel;
import ch.rweiss.seltec.taf3.export.excel.Sheet;

public class ExportFinalFastest {

  private final Arguments arguments;
  private final List<Result> results;

  public ExportFinalFastest(Arguments arguments, List<Result> results) {
    this.arguments = arguments;
    this.results = results;
  }

  public void export() throws IOException {
    var competitions = Competition.fromResults(results);
    for (var competition : competitions) {
      String fileName = competition.discipline()+"-"+competition.category()+"-results.xlsx";
      var outputFile = arguments.outputPath().resolve(fileName);
      System.out.print("Schreibe Datei " + outputFile + " ... ");
      try (var excel = new Excel(outputFile)) {
        var sheet = excel.createSheet(competition.discipline()+"-"+competition.category());
        writeResults(sheet, competition);
      }
      System.out.println("erledigt.");
    }
  }

  private void writeResults(Sheet sheet, Competition competition) {
    var rowNr = 0;
    writeHeader(sheet, rowNr++);
    var res = results
            .stream()
            .filter(result -> result.competition().equals(competition))
            .sorted(Result.RANK_COMPERATOR)
            .toList();
    for (var result : res) {
      writeResult(sheet, competition, result, rowNr++);
    }
  }

  private void writeHeader(Sheet sheet, int rowNr) {
    var row = sheet.createRow(rowNr);
    var cellNr = 0;
    row.createHeaderCell(cellNr++, 5, "Rank");
    row.createHeaderCell(cellNr++, 13, "Start Number");
    row.createHeaderCell(cellNr++, 20, "Last Name");
    row.createHeaderCell(cellNr++, 20, "First Name");
    row.createHeaderCell(cellNr++, 12, "Year of Birth");
    row.createHeaderCell(cellNr++, 6, "Nation");
    row.createHeaderCell(cellNr++, 20, "Club");
    row.createHeaderCell(cellNr++, 10, "Category");
    row.createHeaderCell(cellNr++, 10, "Discipline");
    row.createHeaderCell(cellNr++, 6, "Info");
    row.createHeaderCell(cellNr++, 8, "Result");
  }

  private void writeResult(Sheet sheet, Competition competition, Result result, int rowNr) {
    var row = sheet.createRow(rowNr++);
    var cellNr = 0;
    row.createResultCell(cellNr++, result.rank());
    row.createResultCell(cellNr++, result.athlete().startNumber());
    row.createResultCell(cellNr++, result.athlete().lastName());
    row.createResultCell(cellNr++, result.athlete().firstName());
    row.createResultCell(cellNr++, result.athlete().yearOfBirth());
    row.createResultCell(cellNr++, result.athlete().nation());
    row.createResultCell(cellNr++, result.athlete().clubName());
    row.createResultCell(cellNr++, competition.category());
    row.createResultCell(cellNr++, competition.discipline());
    row.createResultCell(cellNr++, "Final");
    row.createResultCell(cellNr++, result.value());
  }
}
