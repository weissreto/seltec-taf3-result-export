package ch.rweiss.seltec.taf3.export;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;

public class SeltecTaf3ExportToExcel {

  private static final int CHAR_WIDTH = 256;
  private final Path resultsFilePath;
  private final Path outputFilePath;

  public SeltecTaf3ExportToExcel(String resultFilePath, String outputFilePath) {
    this.resultsFilePath = Path.of(resultFilePath);
    this.outputFilePath = Path.of(outputFilePath);
  }

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("Please provide an exported result *.csv file from Seltec TAF 3");
      System.exit(1);
    }
    try {
      new SeltecTaf3ExportToExcel(args[0], System.getProperty("user.dir")).generateResults();
    }
    catch(Exception ex) {
      System.err.println("Could not generate per category and discipline result *.csv files");
      System.err.println("Reason:");
      ex.printStackTrace();
      System.exit(2);
    }
    System.exit(0);
  }

  void generateResults() throws IOException, InvalidFormatException {
    var results = Result.readFrom(resultsFilePath);
    var competitions = Competition.fromResults(results);
    try (var outputStream = Files.newOutputStream(outputFilePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)){
      try (var workbook = new XSSFWorkbook(XSSFWorkbookType.XLSX)) {
	    for (var competition : competitions) {
	      writeResults(workbook, competition, results);
	    }  	  
	    workbook.write(outputStream);
	  }
    }
  }

  private void writeResults(XSSFWorkbook workbook, Competition competition, List<Result> results) throws IOException {
    results = new ArrayList<>(results
        .stream()
        .filter(result -> result.competition().equals(competition))
        .toList());
    Collections.sort(results, Result.RANK_COMPERATOR);
    var sheet = workbook.createSheet(competition.discipline()+"-"+competition.category());
	int rowNr = 0;
	writeTitle(workbook, competition, sheet, rowNr++);
    writeHeader(workbook, sheet, rowNr++);
    var resultStyle = resultStyle(workbook);
    for (var result : results) {
      writeResult(sheet, result, resultStyle, rowNr++);
    }
  }
  
  private void writeResult(XSSFSheet sheet, Result result, XSSFCellStyle resultStyle, int rowNr) {
	var row = sheet.createRow(rowNr++);
	writeResultCell(row, 0, resultStyle, result.rank());
	writeResultCell(row, 1, resultStyle, result.athlete().startNumber());
	writeResultCell(row, 2, resultStyle, result.athlete().lastName());
	writeResultCell(row, 3, resultStyle, result.athlete().firstName());
	writeResultCell(row, 4, resultStyle, result.athlete().yearOfBirth());
	writeResultCell(row, 5, resultStyle, result.athlete().nation());
	writeResultCell(row, 6, resultStyle, result.athlete().clubName());
	writeResultCell(row, 7, resultStyle, result.value());
  }

  private void writeResultCell(XSSFRow row, int column, XSSFCellStyle resultStyle, String value) {
	var cell = row.createCell(column);
	cell.setCellValue(value);
	cell.setCellStyle(resultStyle);  
  }

  private void writeResultCell(XSSFRow row, int column, XSSFCellStyle resultStyle, int value) {
	var cell = row.createCell(column);
	cell.setCellValue(value);
	cell.setCellStyle(resultStyle);  
  }

private int writeTitle(XSSFWorkbook workbook, Competition competition, XSSFSheet sheet, int rowNr) {
    var row = sheet.createRow(rowNr++);
    var cell = row.createCell(0);
    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
    cell.setCellValue("Results " + competition.discipline() + " " + competition.category());
    var titleStyle = titleStyle(workbook);
    row.setHeight((short)1000);
    cell.setCellStyle(titleStyle);
	return rowNr;
  }

  private void writeHeader(XSSFWorkbook workbook, XSSFSheet sheet, int rowNr) {
	var headerStyle = headerStyle(workbook);
	var row = sheet.createRow(rowNr);
    
    var cell = row.createCell(0);
    cell.setCellValue("Rank");
    cell.setCellStyle(headerStyle);
    sheet.setColumnWidth(0, 5 * CHAR_WIDTH);
    
    cell = row.createCell(1);
    cell.setCellValue("Start Number");
    cell.setCellStyle(headerStyle);
    sheet.setColumnWidth(1, 13 * CHAR_WIDTH);
    
    cell = row.createCell(2);
    cell.setCellValue("Last Name");
    cell.setCellStyle(headerStyle);
    sheet.setColumnWidth(2, 20 * CHAR_WIDTH);
    
    cell = row.createCell(3);
    cell.setCellValue("First Name");
    cell.setCellStyle(headerStyle);
    sheet.setColumnWidth(3, 20 * CHAR_WIDTH);
    
    cell = row.createCell(4);
    cell.setCellValue("Year of Birth");
    cell.setCellStyle(headerStyle);
    sheet.setColumnWidth(4, 12 * CHAR_WIDTH);
    
    cell = row.createCell(5);
    cell.setCellValue("Nation");
    cell.setCellStyle(headerStyle);
    sheet.setColumnWidth(5, 6 * CHAR_WIDTH);
    
    cell = row.createCell(6);
    cell.setCellValue("Club");
    cell.setCellStyle(headerStyle);
    sheet.setColumnWidth(6, 20 * CHAR_WIDTH);
    
    cell = row.createCell(7);
    cell.setCellValue("Result");
    cell.setCellStyle(headerStyle);
    sheet.setColumnWidth(7, 8 * CHAR_WIDTH);
  }

  private XSSFCellStyle resultStyle(XSSFWorkbook workbook) {
	var valueStyle = workbook.createCellStyle();
    valueStyle.setAlignment(HorizontalAlignment.LEFT);
    valueStyle.setBorderBottom(BorderStyle.HAIR);
    return valueStyle;
  }

  private XSSFCellStyle headerStyle(XSSFWorkbook workbook) {
	var headerStyle = workbook.createCellStyle();
    var headerFont = workbook.createFont();
    headerFont.setBold(true);
    headerStyle.setFont(headerFont);
//    headerStyle.setFillPattern(FillPatternType.FINE_DOTS);
//    headerStyle.setFillBackgroundColor(IndexedColors.LIGHT_BLUE.getIndex());
    headerStyle.setBorderBottom(BorderStyle.MEDIUM);
	return headerStyle;
  }

  private XSSFCellStyle titleStyle(XSSFWorkbook workbook) {
	var titleStyle = workbook.createCellStyle();
    var titleFont = workbook.createFont();
    titleFont.setFontHeight(20);
    titleStyle.setFont(titleFont);
    titleStyle.setAlignment(HorizontalAlignment.CENTER);
    titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	return titleStyle;
  }
}
