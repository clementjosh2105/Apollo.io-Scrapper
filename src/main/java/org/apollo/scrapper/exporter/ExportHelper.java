package org.apollo.scrapper.exporter;

import static org.apollo.scrapper.constants.Constants.*;
import static org.apollo.scrapper.constants.Constants.QUOTES;
import static org.apollo.scrapper.enums.ExporterEnum.*;
import static org.apollo.scrapper.enums.ExporterEnum.EXCEL;
import static org.apollo.scrapper.enums.ExporterEnum.TSV;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;
import org.apollo.scrapper.bean.response.list.ApolloSavedList;
import org.apollo.scrapper.constants.Constants;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class ExportHelper {

  public String getResponse(String requestBody, String url) throws URISyntaxException {
    log.info("Request made to apollo with url {} & rb {}", url, requestBody);
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

  public String getQuotedString(String name) {
    return QUOTES + name + QUOTES;
  }

  public Exporter getExporter(ExportHelper exportHelper, String name) {
    return switch (name) {
      case Constants.EXCEL -> new ExcelExporter(exportHelper, EXCEL);
      case Constants.TSV -> new TSVExporter(exportHelper, TSV);
      default -> new CSVExporter(exportHelper, CSV);
    };
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
        path + File.separator + apolloSavedList.getName() + "-" + new Date().getTime() + extension);
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
