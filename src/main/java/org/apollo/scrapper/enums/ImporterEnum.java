package org.apollo.scrapper.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum ImporterEnum {
  BREVO(
      "BREVO",
      List.of("EMAIL", "FIRSTNAME", "LASTNAME", "DESIGNATION", "COMPANY_NAME"),
      ",",
      ".csv");

  private final String format;
  private final List<String> header;
  private final String delimiter;
  private final String extension;
}
