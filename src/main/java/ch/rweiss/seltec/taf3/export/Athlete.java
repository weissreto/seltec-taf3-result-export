package ch.rweiss.seltec.taf3.export;

import org.apache.commons.csv.CSVRecord;

record Athlete(int startNumber, String lastName, String firstName, String nation, String clubName, int yearOfBirth) {

  public static Athlete fromRecord(CSVRecord record) {
    return new Athlete(
        Integer.parseInt(record.get("Bib")),
        record.get("LastName"),
        record.get("FirstName"),
        record.get("Nation"),
        record.get("ClubName"),
        Integer.parseInt(record.get("Yob")));
  }

}
