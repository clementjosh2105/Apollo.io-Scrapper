package org.apollo.scrapper.importer;

import static org.apollo.scrapper.constants.ApolloConstants.*;
import static org.apollo.scrapper.constants.BrevoConstants.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apollo.scrapper.bean.apollo.response.contacts.ApolloContactResponse;
import org.apollo.scrapper.bean.apollo.response.list.ApolloSavedList;
import org.apollo.scrapper.bean.brevo.request.BrevoContactsImportBean;
import org.apollo.scrapper.bean.brevo.request.BrevoCreateListBean;
import org.apollo.scrapper.bean.brevo.request.BrevoFolderCreateBean;
import org.apollo.scrapper.bean.brevo.response.BrevoCreateFolderOrListResponseBean;
import org.apollo.scrapper.bean.brevo.response.BrevoFolderInfoResponseBean;
import org.apollo.scrapper.bean.brevo.response.BrevoFolderResponseBean;
import org.apollo.scrapper.constants.Constants;
import org.apollo.scrapper.enums.ImporterEnum;

@Slf4j
@AllArgsConstructor
public class BrevoImporter implements Importer {

  private final ImportHelper importHelper;
  private final ImporterEnum importerEnum;

  private static String getListName() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM hh:mm a");
    return simpleDateFormat.format(new Date());
  }

  private Date getDate() throws ParseException {
    Scanner scanner = new Scanner(System.in);
    String startDateStr = scanner.nextLine();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    return simpleDateFormat.parse(startDateStr);
  }

  private Date getStartDate(String date) throws ParseException {
    System.out.println("Enter the " + date + " date: ");
    return getDate();
  }

  @Override
  public void importApolloList(ApolloSavedList apolloSavedList)
      throws URISyntaxException, IOException, ParseException {
    final int iterationCount = (int) Math.ceil((double) apolloSavedList.getCachedCount() / 100);
    int folderId = checkAndGetFolderId(apolloSavedList.getName());
    System.out.println("Import criteria");
    System.out.println("Press 1 to filter based on date range");
    System.out.println("Press 2 process all records");
    Scanner scanner = new Scanner(System.in);
    String ip = scanner.nextLine();
    switch (ip) {
      case "1":
        Date startDate = getStartDate("start");
        log.info("Start date from input: {}", startDate);
        Date endDate = getStartDate("end");
        log.info("End date from input: {}", endDate);
        for (int i = 1; i <= iterationCount; i++) {
          processContacts(folderId, startDate, endDate, apolloSavedList, i);
        }
        break;
      case "2":
        for (int i = 1; i <= iterationCount; i++) {
          processContacts(folderId, null, null, apolloSavedList, i);
        }
        break;
      default:
        System.out.println("Invalid option.");
        break;
    }
  }

  private int checkAndGetFolderId(String name) throws URISyntaxException, JsonProcessingException {
    String json = importHelper.getResponse(GET_FOLDER_URL);
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    BrevoFolderResponseBean brevoFolderResponseBean =
        mapper.readValue(json, BrevoFolderResponseBean.class);
    Optional<BrevoFolderInfoResponseBean> brevoFolderInfoResponseBeanOptional =
        brevoFolderResponseBean.getFolders().stream()
            .filter(
                brevoFolderInfoResponseBean -> brevoFolderInfoResponseBean.getName().equals(name))
            .findFirst();
    if (brevoFolderInfoResponseBeanOptional.isPresent())
      return brevoFolderInfoResponseBeanOptional.get().getId();
    json =
        importHelper.getResponse(
            BrevoFolderCreateBean.builder().name(name).build(), CREATE_FOLDER_URL);
    BrevoCreateFolderOrListResponseBean brevoCreateFolderOrListResponseBean =
        mapper.readValue(json, BrevoCreateFolderOrListResponseBean.class);
    return brevoCreateFolderOrListResponseBean.getId();
  }

  private void processContacts(
      int folderId, Date startDate, Date endDate, ApolloSavedList apolloSavedList, int batchCount)
      throws URISyntaxException, IOException {
    final String requestBody =
        String.format(REQUEST_FOR_CONTACT_LIST, apolloSavedList.getId(), batchCount);
    String json = importHelper.getApolloResponse(requestBody, CONTACT_LIST_URL);
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    ApolloContactResponse apolloContactResponse =
        mapper.readValue(json, ApolloContactResponse.class);
    StringBuilder importContactsString =
        new StringBuilder(
            String.join(importerEnum.getDelimiter(), importerEnum.getHeader())
                + Constants.LINE_BREAK);
    boolean isContactAdded = false;
    for (int i = 0; i < apolloContactResponse.getContacts().size(); i++) {

      Date createdDate = apolloContactResponse.getContacts().get(i).getCreatedAt();
      if (!((createdDate.equals(startDate) || createdDate.after(startDate))
          && (createdDate.before(endDate) || createdDate.equals(endDate)))) continue;
      String fName = apolloContactResponse.getContacts().get(i).getFName();
      String lName = apolloContactResponse.getContacts().get(i).getLName();
      String organizationName = apolloContactResponse.getContacts().get(i).getOrganizationName();
      String title = apolloContactResponse.getContacts().get(i).getTitle();
      String email = apolloContactResponse.getContacts().get(i).getEmail();
      log.info("Record with name {} created at {} ", fName, createdDate);
      if (Objects.toString(email, "").isEmpty()) continue;
      importContactsString
          .append(String.join(",", email, fName, lName, title, organizationName))
          .append(Constants.LINE_BREAK);
      if (!isContactAdded) isContactAdded = true;
    }
    if (isContactAdded) {
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      BrevoCreateListBean brevoCreateListBean =
          BrevoCreateListBean.builder().name(getListName()).folderId(folderId).build();
      json = importHelper.getResponse(brevoCreateListBean, CREATE_LIST_URL);
      BrevoCreateFolderOrListResponseBean brevoCreateFolderOrListResponseBean =
          mapper.readValue(json, BrevoCreateFolderOrListResponseBean.class);
      int listId = brevoCreateFolderOrListResponseBean.getId();

      BrevoContactsImportBean brevoContactsImportBean =
          BrevoContactsImportBean.builder()
              .listIds(List.of(listId))
              .fileBody(String.valueOf(importContactsString))
              .build();
      importHelper.getResponse(brevoContactsImportBean, IMPORT_CONTACTS_URL);
      log.info("Contacts imported successfully");
    } else {
      log.info("No contacts to import");
    }
  }
}
