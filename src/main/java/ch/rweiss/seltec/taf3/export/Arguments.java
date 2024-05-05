package ch.rweiss.seltec.taf3.export;

import java.nio.file.Path;

public record Arguments (String format, Path inputFile, Path outputPath) {

  public static Arguments parse(String[] args) {
    if (args.length != 3) {
      throw new RuntimeException("Falsche Anzahl Argumente. Erwarte drei Argumente");
    }
    return new Arguments(args[0], Path.of(args[1]), Path.of(args[2]));
  }}
