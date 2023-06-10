package ch.rweiss.seltec.taf3.export;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestSeltecTaf3ExportToExcel {

  Path path = Path.of("c:/temp");
  Path inputFilePath;

  @BeforeEach
  void before() throws IOException {

    inputFilePath = path.resolve("seltec-result-export.csv");
    try (var is = TestSeltecTaf3ExportToExcel.class.getResourceAsStream("seltec-result-export.csv")) {
      Files.copy(is, inputFilePath, StandardCopyOption.REPLACE_EXISTING);
    }
    System.setProperty("user.dir", path.toAbsolutePath().toString());
  }

  @Test
  void test() throws IOException, InvalidFormatException {
	var outputFilePath = path.resolve("Results.xlsx");  
    new SeltecTaf3ExportToExcel(inputFilePath.toAbsolutePath().toString(), outputFilePath.toAbsolutePath().toString()).generateResults();
    assertThat(Files.list(path).count()).isEqualTo(1);
  }
}