package ch.rweiss.seltec.taf3.export;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
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
  void test() throws IOException {
    new SeltecTaf3Export(inputFilePath.toAbsolutePath().toString(), path.toAbsolutePath().toString()).generateResults();
    assertThat(Files.list(path).count()).isEqualTo(73);
    Path result = path.resolve("60M-W8-results.csv");
    assertThat(Files.readString(result, StandardCharsets.ISO_8859_1)).isEqualTo("""
        Rank,StartNumber,LastName,FirstName,Yob,Nation,ClubName,Result
        1,93,Hodel,Jana,2015,SUI,Küssnacht am rigi,9.85
        2,13,Beerli,Ulla,2015,SUI,STV Küssnacht,9.86
        3,233,Vonesch,Malin,2015,SUI,Küssnacht am Rigi,9.87
        4,24,Blickisdorf,Nora,2015,SUI,Turnverein Küssnacht,9.89
        5,70,Forster,Celine,2015,SUI,Küssnacht,9.91
        6,66,Eyholzer,Sophia,2015,SUI,Merlischachen,9.92
        7,95,Hofstetter,Shira,2015,SUI,Küssnacht am Rigi,9.93
        8,210,Sidler,Melina,2015,SUI,Küssacht,9.94
        9,20,Birchler,Isabelle,2015,SUI,TV Küssnacht,9.95
        10,126,Kündig,Lenia,2015,SUI,Tv Ibach,9.96
        11,106,Iten,Lena,2015,SUI,tv küssnacht,9.97
        12,71,Franco,Elena,2015,SUI,Turnverein Küssnacht,9.98
        13,104,Iten,Erin,2015,SUI,tv Küssnacht,9.99
        14,195,Schindler,Jaden,2015,SUI,Fc Küssnacht,10.00
        15,85,Hammer,Viktoria,2015,SUI,Küssnacht am Rigi,10.01
        16,138,McCarthy,Marlene,2015,SUI,Leichtathletik Arth,10.02
        17,168,Räber,Luisa,2015,SUI,VBC Küssnacht,10.03
        18,97,Husaini,Hasti,2015,SUI,Schwyz,10.04
        19,17,Betschart,Lya,2015,SUI,Immensee,10.05
        20,8,Bachmann,Mara,2015,SUI,Merlischachen,10.06
        21,119,Kriebel,Sienna,2015,SUI,Küssnacht am Rigi,10.07
        22,141,Messerli,Chloé,2015,SUI,Küssnacht am Rigi,10.08
        """.replace("\n", "\r\n"));
  }
}
