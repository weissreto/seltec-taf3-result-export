package ch.rweiss.seltec.taf3.export.excel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;

public class Excel implements AutoCloseable {

  private final XSSFWorkbook workbook;
  private final Map<Style, XSSFCellStyle> styles = new HashMap<>();
  private final Path outputFile;

  public Excel(Path outputFile) {
    this.outputFile = outputFile;
    workbook = new XSSFWorkbook(XSSFWorkbookType.XLSX);
  }

  @Override
  public void close() throws IOException {
    Files.createDirectories(outputFile.getParent());
    try (var outputStream = Files.newOutputStream(outputFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
      workbook.write(outputStream);
      workbook.close();
    }
  }

  public Sheet createSheet(String name) {
    return new Sheet(this, workbook.createSheet(name));
  }

  XSSFCellStyle style(Style style) {
    return styles.computeIfAbsent(style, this::createStyle);
  }

  private XSSFCellStyle createStyle(Style style) {
    var cellStyle = workbook.createCellStyle();
    var font = workbook.createFont();
    cellStyle.setFont(font);
    switch(style) {
      case TITLE -> {
        font.setFontHeight(20);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
      }
      case HEADER -> {
        font.setBold(true);
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
      }
      case RESULT -> {
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setBorderBottom(BorderStyle.HAIR);
      }
    }
    return cellStyle;
  }
}
