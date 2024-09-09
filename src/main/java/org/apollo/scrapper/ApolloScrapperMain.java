package org.apollo.scrapper;

import static org.apollo.scrapper.constants.Constants.*;

import java.io.IOException;
import java.net.URISyntaxException;
import org.apollo.scrapper.process.ApolloScrappingProcess;

public class ApolloScrapperMain {

  public static void main(String[] args)
      throws IOException, URISyntaxException, InterruptedException {
    clearScreen();
    System.out.println(BANNER);
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
}
