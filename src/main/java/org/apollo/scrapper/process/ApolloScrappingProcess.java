package org.apollo.scrapper.process;

import static org.apollo.scrapper.constants.Constants.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Console;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;
import org.apollo.scrapper.bean.response.list.ApolloSavedList;
import org.apollo.scrapper.bean.response.list.ApolloSavedListResponse;
import org.apollo.scrapper.exporter.ExportHelper;
import org.apollo.scrapper.exporter.Exporter;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class ApolloScrappingProcess {

  public void clearScreen() {
    try {
      if (System.getProperty("os.name").contains("Windows")) {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
      } else {
        new ProcessBuilder("clear").inheritIO().start().waitFor();
      }
    } catch (IOException | InterruptedException ex) {
      ex.printStackTrace();
    }
  }

  private String getResponse(String requestBody, String url) throws URISyntaxException {
    log.info("Request made to apollo with url {}", url);
    RestTemplate restTemplate = new RestTemplate();
    URI uri = new URI(url);
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    headers.set(X_API_KEY, API_KEY);
    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
    ResponseEntity<String> result =
        restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);

    return result.getBody();
  }

  public ApolloSavedList getListInfo(String listName)
      throws URISyntaxException, JsonProcessingException {
    final String requestBody = String.format(REQUEST_FOR_SAVED_LIST, listName);
    String json = getResponse(requestBody, SAVED_LIST_URL);
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    ApolloSavedListResponse apolloSavedListResponse =
        mapper.readValue(json, ApolloSavedListResponse.class);
    return apolloSavedListResponse.getLabels().get(0);
  }

  public String getListName() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter the list name you want to export: ");
    return scanner.nextLine();
  }

  public void authenticate(int attempts) {
    if (attempts > 3)
      throw new RuntimeException(
          "Max attempts for login exceeded. Please try again after sometime.");
    try {
      Scanner scanner = new Scanner(System.in);
      Console console = System.console();

      System.out.println("Enter your username: ");
      String username = scanner.nextLine();
      char[] passwordArray = console.readPassword("Enter your password: ");
      String password = new String(passwordArray);
      final String requestBody = String.format(REQUEST_FOR_LOGIN, username, password);
      getResponse(requestBody, LOGIN_URL);
    } catch (Exception e) {
      log.error("Unable to login. Kindly check the credentials.");
      ++attempts;
      authenticate(attempts);
    }
  }

  public void start(int attempts) throws URISyntaxException, JsonProcessingException {
//    clearScreen();
    if (attempts > 3) {
      log.error("Max attempts for login exceeded. Please try again after sometime.");
      System.exit(0);
    }

    String listName = getListName();
    log.info("Processing the list '{}'", listName);
    ApolloSavedList listInfo = getApolloSavedList(listName);
    if (Objects.toString(listInfo.getId(), "").isEmpty()) {
      log.error("The requested list is not present. Try entering a valid list name");
      ++attempts;
      printMenu(attempts, true);
    }
    if (listInfo.getCachedCount() > 0) {
      ExportHelper exportHelper = new ExportHelper();
      Exporter exporter = exportHelper.getExporter(exportHelper, EXCEL);
      exporter.export(listInfo);
      printMenu(attempts, false);
    } else {
      log.error("The requested list is not empty with no records. Try entering a valid list name");
      ++attempts;
      printMenu(attempts, true);
    }
  }

  private ApolloSavedList getApolloSavedList(String listName) {
    ApolloSavedList listInfo = ApolloSavedList.builder().build();
    try {
      listInfo = getListInfo(listName);
    } catch (Exception ignored) {
    }
    return listInfo;
  }

  private void printMenu(int attempts, boolean isError)
      throws URISyntaxException, JsonProcessingException {
    clearScreen();
    System.out.println("Export another list? Press 'Y' to proceed and any other key to quit");
    Scanner scanner = new Scanner(System.in);
    String input = scanner.nextLine();
    if (input.equalsIgnoreCase("y")) start(isError ? attempts : START_ATTEMPT_COUNT_LIST);
    else System.exit(0);
  }
}
