package ch.rweiss.seltec.taf3.export;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class TestSeltecTaf3Export {

  @TempDir
  Path path;
  Path inputFilePath;

  @BeforeEach
  void before() throws IOException {

    inputFilePath = path.resolve("seltec-result-export.csv");
    try (var is = TestSeltecTaf3Export.class.getResourceAsStream("seltec-result-export.csv")) {
      Files.copy(is, inputFilePath);
    }
    System.setProperty("user.dir", path.toAbsolutePath().toString());
  }

  @Test
  @Disabled
  void anonymousAthletes() throws IOException {
    Path output = path.resolve("seltec-result-export-anonymous.csv");
    try (var reader = Files.newBufferedReader(inputFilePath, StandardCharsets.ISO_8859_1)) {
      var csvReader = Result.SELTEC_RESULT_EXPORT_FORMAT.parse(reader);
      try (var writer = Files.newBufferedWriter(output, StandardCharsets.ISO_8859_1)) {
        var printer = Result.SELTEC_RESULT_EXPORT_FORMAT.print(writer);
        var headers = csvReader.getHeaderNames();
        printer.printRecord(headers);
        var records = csvReader.getRecords();
        for (var record : records) {
          var values = record.values();
          values[3] = abrevate(values[3]);
          values[4] = abrevate(values[4]);
          printer.printRecord((Object[])values);
        }
      }
    }
    var content = Files.readString(output, StandardCharsets.ISO_8859_1);
    content = content.replace("\"", "");
    Files.writeString(output, content, StandardCharsets.ISO_8859_1);
    System.out.println("Written "+output);
  }

  private String abrevate(String string) {
    if (string.length() > 2) {
      return string.substring(0, 2);
    }
    return string;
  }

  @Test
  void test() throws IOException {
    new SeltecTaf3Export(inputFilePath.toAbsolutePath().toString(), path.toAbsolutePath().toString()).generateResults();
    assertThat(Files.list(path).count()).isEqualTo(73);
    Path result = path.resolve("60M-W8-results.csv");
    assertThat(Files.readString(result, StandardCharsets.ISO_8859_1)).isEqualTo("""
        Rank,StartNumber,LastName,FirstName,Yob,Nation,ClubName,Result
        1,108,Hu,Sa,2015,SUI,TV Ibach,10.55
        2,14,Be,Ul,2015,SUI,STV Küssnacht,10.65
        3,94,Ha,Vi,2015,SUI,Küssnacht am Rigi,10.76
        4,291,Ul,Lo,2015,SUI,TV Küssnacht,10.79
        5,152,Mc,Ma,2015,SUI,Leichtathletik Arth,10.8
        6,229,Si,Me,2015,SUI,Küssacht,10.8
        7,21,Bi,Is,2015,SUI,TV Küssnacht,10.82
        8,25,Bi,Li,2015,SUI,kein Verein,10.96
        9,138,Kü,Le,2015,SUI,Tv Ibach,10.99
        10,278,Ja,Sw,2015,SUI,TV Küssnacht,11.16
        11,253,Vo,Ma,2015,SUI,Küssnacht am Rigi,11.28
        12,102,Ho,Ja,2015,SUI,Küssnacht am rigi,11.37
        13,182,Rä,Lu,2015,SUI,VBC Küssnacht,11.4
        14,73,Ey,So,2015,SUI,Merlischachen,11.53
        14,155,Me,Ch,2015,SUI,Küssnacht am Rigi,11.53
        16,130,Kr,Si,2015,SUI,Küssnacht am Rigi,11.71
        17,77,Fo,Ce,2015,SUI,Küssnacht,11.8
        18,9,Ba,Ma,2015,SUI,Merlischachen,11.85
        19,18,Be,Ly,2015,SUI,Immensee,11.86
        20,78,Fr,El,2015,SUI,Turnverein Küssnacht,11.88
        21,104,Ho,Sh,2015,SUI,Küssnacht am Rigi,11.92
        22,47,Ca,Al,2015,SUI,Küssnacht,11.98
        23,107,Hu,Ha,2015,SUI,Schwyz,12
        24,26,Bl,No,2015,SUI,Turnverein Küssnacht,12.36
        25,50,Ca,Si,2015,SUI,-,12.92
        26,115,It,Er,2015,SUI,tv Küssnacht,13.31
        27,117,It,Le,2015,SUI,tv küssnacht,13.34
        """.replace("\n", "\r\n"));
  }
}
