package ch.rweiss.seltec.taf3.export;

import java.io.IOException;
import java.util.List;
import ch.rweiss.seltec.taf3.export.excel.Excel;
import ch.rweiss.seltec.taf3.export.excel.Sheet;
import ch.rweiss.seltec.taf3.export.excel.Style;

public class ExportResults {

  private final Arguments arguments;
  private final List<Result> results;

  public ExportResults(Arguments arguments, List<Result> results) {
    this.arguments = arguments;
    this.results = results;
  }

  public void export() throws IOException {
    var competitions = Competition.fromResults(results);
    System.out.print("Schreibe Datei "+ arguments.outputPath() + " ... ");
    try (var excel = new Excel(arguments.outputPath())) {
      for (var competition : competitions) {
        writeTitleAndResults(excel, competition);
      }
    }
    System.out.println("erledigt.");
  }

  private void writeTitleAndResults(Excel excel, Competition competition) {
    var sheet = excel.createSheet(competition.discipline()+"-"+competition.category());
    writeTitle(sheet, competition, 0);
    writeResults(sheet, competition, 1);
  }

  private void writeTitle(Sheet sheet, Competition competition, int rowNr) {
    var row = sheet.createRow(rowNr++);
    row.height((short)1000);
    var cell = row.createCell(0);
    cell.mergeTo(1, 8);
    cell.value("Results " + competition.discipline() + " " + competition.category());
    cell.style(Style.TITLE);
  }

  private void writeResults(Sheet sheet, Competition competition, int rowNr) {
    writeHeader(sheet, rowNr++);
    var res = results
            .stream()
            .filter(result -> result.competition().equals(competition))
            .sorted(Result.RANK_COMPERATOR)
            .toList();
    for (var result : res) {
      writeResult(sheet, result, rowNr++);
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
    row.createHeaderCell(cellNr++, 8, "Result");
  }

  private void writeResult(Sheet sheet, Result result, int rowNr) {
    var row = sheet.createRow(rowNr++);
    var cellNr = 0;
    row.createResultCell(cellNr++, result.rank());
    row.createResultCell(cellNr++, result.athlete().startNumber());
    row.createResultCell(cellNr++, result.athlete().lastName());
    row.createResultCell(cellNr++, result.athlete().firstName());
    row.createResultCell(cellNr++, result.athlete().yearOfBirth());
    row.createResultCell(cellNr++, result.athlete().nation());
    row.createResultCell(cellNr++, result.athlete().clubName());
    row.createResultCell(cellNr++, result.value());
  }
}