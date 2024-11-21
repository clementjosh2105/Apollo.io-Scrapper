package org.apollo.scrapper;

import static org.apollo.scrapper.constants.Constants.*;
import static org.apollo.scrapper.constants.ApolloConstants.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import org.apollo.scrapper.process.ApolloScrappingProcess;

public class ApolloScrapperMain {

  public static void main(String[] args)
      throws IOException, URISyntaxException, InterruptedException, ParseException {
    clearScreen();
    System.out.println(BANNER);
    if (API_KEY == null || API_KEY.isEmpty()) {
      Scanner scanner = new Scanner(System.in);
      System.out.println("API key not found. Kindly enter you API Key: ");
      API_KEY = scanner.nextLine();
    }

    ApolloScrappingProcess apolloScrappingProcess = new ApolloScrappingProcess();
    //    apolloScrappingProcess.authenticate(START_ATTEMPT_COUNT_LOGIN);
    apolloScrappingProcess.start(START_ATTEMPT_COUNT_LIST);
  }

  public static void clearScreen() {
    try {
      if (System.getProperty("os.name").contains("Windows")) {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
      } else {
        new ProcessBuilder("clear").inheritIO().start().waitFor();
      }
    } catch (IOException | InterruptedException ignored) {

    }
  }

  private static String getListName() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM hh:mm a");
    return simpleDateFormat.format(new Date());
  }

}
