package ch.rweiss.seltec.taf3.export;

record Athlete(int startNumber, String lastName, String firstName, String nation, String clubName, int yearOfBirth) {

  public static Athlete fromRecord(CsvRecord record) {
    return new Athlete(
        record.asInt("Bib"),
        record.asString("LastName"),
        record.asString("FirstName"),
        record.asString("Nation"),
        record.asString("ClubName"),
        record.asInt("Yob"));
  }
}
