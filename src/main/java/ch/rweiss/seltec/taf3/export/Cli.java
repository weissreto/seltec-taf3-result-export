package ch.rweiss.seltec.taf3.export;

import java.io.IOException;

public class Cli {
  private static final int OK = 0;
  private static final int EXCEPTION = -1;

  public static void main(String[] args) {
    System.exit(export(args));
  }

  public static int export(String[] args) {
    try {
      var arguments = Arguments.parse(args);
      export(arguments);
      return OK;
    } catch (Throwable ex) {
      System.out.println();
      System.err.print("Fehler: ");
      System.err.println(ex.getMessage());
      System.out.println();
      help();
      return EXCEPTION;
    }

  }

  private static void export(Arguments arguments) throws IOException {
    var results = Result.readFrom(arguments.inputFile());
    switch(arguments.format()) {
      case "Resultate" -> new ExportResults(arguments, results).export();
      case "FinalSchnellsti" -> new ExportFinalFastest(arguments, results).export();
      default -> throw new RuntimeException("Unbekanntes Format "+arguments.format());
    }
  }

  private static void help() {
    System.out.println("Hilfe:");
    System.out.println("SeltecTaf3Export [Format] [Taf3ResultExportDatei] [AusgabePfad]");
    System.out.println("Unterstützte Formate:");
    System.out.println("- Resultate: Excel Datei mit einem Tab pro Kategorie und Bewerb");
    System.out.println("- FinalSchnellsti: Excel Datei pro Kategorie und Bewerb zum editieren und reimport in TAF3 für die Finals vom Schnellsti");
    System.out.println("Beispiele:");
    System.out.println("SeltecTaf3Export Resultate seltec-result-export.csv Resultate.xlsx");
    System.out.println("SeltecTaf3Export FinalSchnellsti seltec-result-export.csv \\Schnellsti\\Final");
  }
}
