package ch.rweiss.seltec.taf3.export;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class TestExportFinalFastest {

  static List<List<String>> RESULTS = toTable("""
          Rank,Start Number,Last Name,First Name,Year of Birth,Nation,Club,Category,Discipline,Info,Result
          1,108,Hu,Sa,2015,SUI,TV Ibach,W8,60M,Final,10.55
          2,14,Be,Ul,2015,SUI,STV Küssnacht,W8,60M,Final,10.65
          3,94,Ha,Vi,2015,SUI,Küssnacht am Rigi,W8,60M,Final,10.76
          4,291,Ul,Lo,2015,SUI,TV Küssnacht,W8,60M,Final,10.79
          5,152,Mc,Ma,2015,SUI,Leichtathletik Arth,W8,60M,Final,10.8
          6,229,Si,Me,2015,SUI,Küssacht,W8,60M,Final,10.8
          7,21,Bi,Is,2015,SUI,TV Küssnacht,W8,60M,Final,10.82
          8,25,Bi,Li,2015,SUI,kein Verein,W8,60M,Final,10.96
          9,138,Kü,Le,2015,SUI,Tv Ibach,W8,60M,Final,10.99
          10,278,Ja,Sw,2015,SUI,TV Küssnacht,W8,60M,Final,11.16
          11,253,Vo,Ma,2015,SUI,Küssnacht am Rigi,W8,60M,Final,11.28
          12,102,Ho,Ja,2015,SUI,Küssnacht am rigi,W8,60M,Final,11.37
          13,182,Rä,Lu,2015,SUI,VBC Küssnacht,W8,60M,Final,11.4
          14,73,Ey,So,2015,SUI,Merlischachen,W8,60M,Final,11.53
          14,155,Me,Ch,2015,SUI,Küssnacht am Rigi,W8,60M,Final,11.53
          16,130,Kr,Si,2015,SUI,Küssnacht am Rigi,W8,60M,Final,11.71
          17,77,Fo,Ce,2015,SUI,Küssnacht,W8,60M,Final,11.8
          18,9,Ba,Ma,2015,SUI,Merlischachen,W8,60M,Final,11.85
          19,18,Be,Ly,2015,SUI,Immensee,W8,60M,Final,11.86
          20,78,Fr,El,2015,SUI,Turnverein Küssnacht,W8,60M,Final,11.88
          21,104,Ho,Sh,2015,SUI,Küssnacht am Rigi,W8,60M,Final,11.92
          22,47,Ca,Al,2015,SUI,Küssnacht,W8,60M,Final,11.98
          23,107,Hu,Ha,2015,SUI,Schwyz,W8,60M,Final,12
          24,26,Bl,No,2015,SUI,Turnverein Küssnacht,W8,60M,Final,12.36
          25,50,Ca,Si,2015,SUI,-,W8,60M,Final,12.92
          26,115,It,Er,2015,SUI,tv Küssnacht,W8,60M,Final,13.31
          27,117,It,Le,2015,SUI,tv küssnacht,W8,60M,Final,13.34
          """);

  @TempDir
  Path path;
  Path inputFilePath;

  @BeforeEach
  void before() throws IOException {
    inputFilePath = path.resolve("seltec-result-export.csv");
    try (var is = TestExportFinalFastest.class.getResourceAsStream("seltec-result-export.csv")) {
      Files.copy(is, inputFilePath, StandardCopyOption.REPLACE_EXISTING);
    }
  }

  @Test
  void results() throws IOException {
    var outputPath = path.resolve("results");
    Cli.export(new String[] {
        "FinalSchnellsti",
        inputFilePath.toAbsolutePath().toString(),
        outputPath.toAbsolutePath().toString()});
    assertThat(Files.list(outputPath).count()).isEqualTo(72);
    try (var inputStream = Files.newInputStream(outputPath.resolve("60M-W8-results.xlsx"))){
      try (var workbook = new XSSFWorkbook(inputStream)) {
        assertThat(workbook.getNumberOfSheets()).isEqualTo(1);
        var sheet = workbook.getSheetAt(0);
        assertThat(sheet.getSheetName()).isEqualTo("60M-W8");

        assertThat(sheet.getLastRowNum()).isEqualTo(RESULTS.size()-1);
        for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
          var result = RESULTS.get(rowNum);
          var row = sheet.getRow(rowNum);
          assertThat(row.getLastCellNum()).isEqualTo((short)result.size());
          for (int cellNum = 0; cellNum < row.getLastCellNum(); cellNum++) {
            var cell = row.getCell(cellNum);
            if (cell.getCellType() == CellType.STRING) {
              assertThat(cell.getStringCellValue())
                  .as("Row " + rowNum +" Cell " + cellNum)
                  .isEqualTo(result.get(cellNum));
            } else {
              assertThat(Integer.toString((int)cell.getNumericCellValue()))
                  .as("Row " + rowNum +" Cell " + cellNum)
                  .isEqualTo(result.get(cellNum));
            }
          }
        }
      }
    }
  }

  static List<List<String>> toTable(String result) {
    return Stream
        .of(result.split("\n"))
        .map(TestExportFinalFastest::toRow)
        .toList();
  }

  static List<String> toRow(String result) {
    return List.of(result.split(","));
  }
}