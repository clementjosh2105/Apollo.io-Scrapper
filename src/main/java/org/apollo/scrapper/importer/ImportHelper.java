package org.apollo.scrapper.importer;

import static org.apollo.scrapper.enums.ImporterEnum.BREVO;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;
import org.apollo.scrapper.bean.apollo.response.list.ApolloSavedList;
import org.apollo.scrapper.constants.ApolloConstants;
import org.apollo.scrapper.constants.BrevoConstants;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class ImportHelper {

  public String getResponse(Object requestBody, String url) throws URISyntaxException {
    log.info("Request made to Brevo with url {}", url);
    RestTemplate restTemplate = new RestTemplate();
    URI uri = new URI(url);
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    headers.set(BrevoConstants.API_KEY_HEADER, BrevoConstants.API_KEY);
    HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);
    ResponseEntity<String> result =
        restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);

    log.info("Response to Brevo with url {}", result.getBody());
    return result.getBody();
  }

  public String getResponse(String url) throws URISyntaxException {
    log.info("Request made to Brevo with url {}", url);
    RestTemplate restTemplate = new RestTemplate();
    URI uri = new URI(url);
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    headers.set(BrevoConstants.API_KEY_HEADER, BrevoConstants.API_KEY);
    HttpEntity<String> requestEntity = new HttpEntity<>(headers);
    ResponseEntity<String> result =
        restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);

    return result.getBody();
  }

  public String getApolloResponse(String requestBody, String url) throws URISyntaxException {
    log.info("Request made to Apollo with url {}", url);
    RestTemplate restTemplate = new RestTemplate();
    URI uri = new URI(url);
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    headers.set(ApolloConstants.API_KEY_HEADER, ApolloConstants.API_KEY);
    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
    ResponseEntity<String> result =
        restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);

    return result.getBody();
  }

  public Importer getImported(ImportHelper importHelper, String name) {
    return new BrevoImporter(importHelper, BREVO);
  }

  public File getAndCreateFileWithExtensions(ApolloSavedList apolloSavedList, String extension) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter the path you want to save your file: ");
    String path = scanner.nextLine();
    File file = new File(path);
    if (file.exists() && file.isFile()) {
      log.error("Invalid path entered, existing the system");
      System.exit(0);
    } else if (!file.exists()) {
      boolean mkdir = file.mkdirs();
      if (mkdir) log.info("Created directory with path {}", path);
    }
    return new File(
        path + File.separator + apolloSavedList.getName() + "(" + new Date() + ")" + extension);
  }

  public int getProcessedRecordCount(int i, int iterationCount, int totalRecordCount) {
    if (totalRecordCount < 100) return totalRecordCount;
    else if (iterationCount == i) {
      int previousRecordCount = (i - 1) * 100;
      return totalRecordCount - previousRecordCount;
    }
    return i * 100;
  }
}
