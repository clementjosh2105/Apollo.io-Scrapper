package org.apollo.scrapper.constants;

public class ApolloConstants {
  /** Apollo constants */
  public static final String CONTACT_LIST_URL = "https://api.apollo.io/v1/mixed_people/search";

  public static final String INDUSTRY_LIST_URL =
      "https://api.apollo.io/api/v1/organizations/load_snippets";
  public static final String SAVED_LIST_URL = "https://app.apollo.io/api/v1/labels/search";
  public static final String LOGIN_URL = "https://app.apollo.io/api/v1/auth/login";

  public static final String REQUEST_FOR_CONTACT_LIST =
      "{\n"
          + "    \"finder_table_layout_id\": \"6668980e82ea4906aef0e370\",\n"
          + "    \"contact_label_ids\": [\n"
          + "        \"%s\"\n"
          + "    ],\n"
          + "    \"prospected_by_current_team\": [\n"
          + "        \"yes\"\n"
          + "    ],\n"
          + "    \"page\": %s,\n"
          + "    \"display_mode\": \"explorer_mode\",\n"
          + "    \"per_page\": 100,\n"
          + "    \"open_factor_names\": [],\n"
          + "    \"num_fetch_result\": 1,\n"
          + "    \"context\": \"people-index-page\",\n"
          + "    \"show_suggestions\": false,\n"
          + "    \"ui_finder_random_seed\": \"zf114oj3ic\",\n"
          + "    \"cacheKey\": 1718117101909\n"
          + "}";

  public static final String REQUEST_FOR_INDUSTRY_NAME =
      "{\n"
          + "    \"ids\": [\n"
          + "        \"%s\"\n"
          + "    ],\n"
          + "    \"cacheKey\": 1718117101909\n"
          + "}";
  public static final String REQUEST_FOR_SAVED_LIST =
      "{\n"
          + "  \"team_lists_only\": [\n"
          + "    \"no\"\n"
          + "  ],\n"
          + "  \"q_name\": \"%s\",\n"
          + "  \"page\": 1,\n"
          + "  \"label_modality\": \"contacts\",\n"
          + "  \"display_mode\": \"explorer_mode\",\n"
          + "  \"per_page\": 50,\n"
          + "  \"open_factor_names\": [\n"
          + "    \n"
          + "  ],\n"
          + "  \"num_fetch_result\": 3,\n"
          + "  \"show_suggestions\": false,\n"
          + "  \"ui_finder_random_seed\": \"osa5xvbu6qb\",\n"
          + "  \"cacheKey\": 1718264896583\n"
          + "}";

  public static final String REQUEST_FOR_LOGIN =
      "{\n"
          + "  \"email\": \"%s\",\n"
          + "  \"password\": \"%s\",\n"
          + "  \"timezone_offset\": -330,\n"
          + "  \"cacheKey\": 1718267668563\n"
          + "}";
  public static final String F_NAME = "First Name";
  public static final String L_NAME = "Last Name";
  public static final String NAME = "Name";
  public static final String TITLE = "Title";
  public static final String EMAIL = "Email";
  public static final String LINKED_IN_URL = "Linked-In URL";
  public static final String COUNTRY = "Country";
  public static final String STATE = "State";
  public static final String CITY = "City";
  public static final String ORGANIZATION_NAME = "Organization Name";
  public static final String INDUSTRY = "Industry";
  public static final String DELIMITER = "::DELIMITER:::";
  public static final String CONTACTS_CSV_HEADER =
      String.join(
          DELIMITER,
          NAME,
          F_NAME,
          L_NAME,
          TITLE,
          ORGANIZATION_NAME,
          INDUSTRY,
          EMAIL,
          LINKED_IN_URL,
          COUNTRY,
          STATE,
          CITY);
  public static final String QUOTES = "\"";
  public static final String API_KEY_HEADER = "X-Api-Key";
  public static final String EXCEPTION_OCCURRED = "Exception occurred: {}";
  public static final int CELL_TYPE_NUMERIC = 0;
  public static final int CELL_TYPE_STRING = 1;
  public static final int CELL_TYPE_FORMULA = 2;
  public static final int CELL_TYPE_BLANK = 3;
  public static final int CELL_TYPE_BOOLEAN = 4;
  public static final int CELL_TYPE_ERROR = 5;
  public static final int COLUMN_COUNT_EXCEL = 11;
  public static final int NAME_INDEX = 0;
  public static final int F_NAME_INDEX = 1;
  public static final int L_NAME_INDEX = 2;
  public static final int ORGANIZATION_NAME_INDEX = 4;
  public static final int TITLE_INDEX = 3;
  public static final int INDUSTRY_INDEX = 5;
  public static final int EMAIL_INDEX = 6;
  public static final int LINKED_IN_INDEX = 7;
  public static final int COUNTRY_INDEX = 8;
  public static final int STATE_INDEX = 9;
  public static final int CITY_INDEX = 10;
  public static String API_KEY = System.getenv("APOLLO_API_KEY");
}
