package org.apollo.scrapper.constants;

public class BrevoConstants {
  /** Brevo constants */
  public static final String API_KEY_HEADER = "api-key";

  public static final String BREVO = "brevo";
  public static final String GET_FOLDER_URL = "https://api.brevo.com/v3/contacts/folders";
  public static final String CREATE_FOLDER_URL = "https://api.brevo.com/v3/contacts/folders";
  public static final String CREATE_LIST_URL = "https://api.brevo.com/v3/contacts/lists";
  public static final String IMPORT_CONTACTS_URL = "https://api.brevo.com/v3/contacts/import";
  public static final String REQUEST_CREATE_FOLDER = "{\n" + "    \"name\": \"%s\",\n" + "}";
  public static String API_KEY = System.getenv("BREVO_API_KEY");
}
